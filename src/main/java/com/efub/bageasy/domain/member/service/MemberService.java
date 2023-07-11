package com.efub.bageasy.domain.member.service;

import com.efub.bageasy.domain.member.domain.Member;
import com.efub.bageasy.domain.member.dto.response.LoginResponseDto;
import com.efub.bageasy.domain.member.oauth.GoogleOAuthToken;
import com.efub.bageasy.domain.member.oauth.GoogleUser;
import com.efub.bageasy.domain.member.repository.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final HttpServletResponse response;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Value("${spring.OAuth2.google.url.login}")
    private String GOOGLE_SNS_LOGIN_URL;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String GOOGLE_SNS_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String GOOGLE_SNS_CALLBACK_URL;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String GOOGLE_SNS_CLIENT_SECRET;

    //@Value("${spring.security.oauth2.client.registration.google.scope}")
    private String GOOGLE_DATA_ACCESS_SCOPE = "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile";

    @Value("${spring.OAuth2.google.url.token}")
    private String GOOGLE_TOKEN_REQUEST_URL;

    @Value("${spring.OAuth2.google.url.profile}")
    private String GOOGLE_USERINFO_REQUEST_URL;


    public void request() throws IOException {
        String redirectURL = getOauthRedirectURL();
        response.sendRedirect(redirectURL);
    }

    public String getOauthRedirectURL() {

        Map<String, Object> params = new HashMap<>();
        params.put("response_type", "code");
        params.put("client_id", GOOGLE_SNS_CLIENT_ID);
        params.put("redirect_uri", GOOGLE_SNS_CALLBACK_URL);
        params.put("scope", GOOGLE_DATA_ACCESS_SCOPE);

        //parameter를 형식에 맞춰 구성해주는 함수
        String parameterString = params.entrySet().stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));
        String redirectURL = GOOGLE_SNS_LOGIN_URL + "?" + parameterString;
        System.out.println("redirectURL = " + redirectURL);

        return redirectURL;
        /*
         * https://accounts.google.com/o/oauth2/v2/auth?scope=profile&response_type=code
         * &client_id="할당받은 id"&redirect_uri="access token 처리")
         * 로 Redirect URL을 생성하는 로직을 구성
         * */
    }

    public LoginResponseDto googleLogin(String code) throws IOException {
        //구글로 일회성 코드를 보내 액세스 토큰이 담긴 응답객체를 받아옴
        //응답 객체가 JSON형식으로 되어 있으므로, 이를 deserialization해서 자바 객체에 담을 것이다.
        GoogleOAuthToken oAuthToken = getAccessToken(code);

        //액세스 토큰을 다시 구글로 보내 구글에 저장된 사용자 정보가 담긴 응답 객체를 받아온다.
        //다시 JSON 형식의 응답 객체를 자바 객체로 역직렬화한다.
        GoogleUser googleUser = getUserInfo(oAuthToken);

        String email = googleUser.getEmail();
        Member member = findMemberByEmail(email);
        Boolean isExistingMember = true;

        //가입 처리
        if (member == null) {
            member = saveMember(googleUser);
            isExistingMember = false;
        }

        //앞으로 회원 인가 처리를 위한 jwtToken을 발급한다.
        String accessToken = jwtTokenProvider.createToken(member.getEmail());

        return new LoginResponseDto(member, accessToken, isExistingMember);
    }

    //일회용 코드를 다시 구글로 보내 액세스 토큰을 포함한 JSON String이 담긴 ResponseEntity를 받아온다.
    public GoogleOAuthToken getAccessToken(String code) throws JsonProcessingException {

        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", GOOGLE_SNS_CLIENT_ID);
        params.put("client_secret", GOOGLE_SNS_CLIENT_SECRET);
        params.put("redirect_uri", GOOGLE_SNS_CALLBACK_URL);
        params.put("grant_type", "authorization_code");

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(GOOGLE_TOKEN_REQUEST_URL,
                params, String.class);
        System.out.println("response.getBody() = " + response.getBody());

        ObjectMapper objectMapper = new ObjectMapper();
        GoogleOAuthToken googleOAuthToken = objectMapper.readValue(response.getBody(), GoogleOAuthToken.class);
        return googleOAuthToken;

    }

    public GoogleUser getUserInfo(GoogleOAuthToken oAuthToken) throws JsonProcessingException {

        //header에 accessToken을 담는다.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + oAuthToken.getAccess_token());

        //HttpEntity를 하나 생성해 헤더를 담아서 restTemplate으로 구글과 통신하게 된다.
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(GOOGLE_USERINFO_REQUEST_URL, HttpMethod.GET, request, String.class);
        System.out.println("response.getBody() = " + response.getBody());

        ObjectMapper objectMapper = new ObjectMapper();
        GoogleUser googleUser = objectMapper.readValue(response.getBody(), GoogleUser.class);
        return googleUser;
    }

    public Member saveMember(@RequestBody GoogleUser googleUser) {
        return memberRepository.save(
                Member.builder()
                        .email(googleUser.getEmail())
                        .nickname(googleUser.getName())
                        .build()
        );
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다. id = " + memberId));
    }

    public Member findMemberByAuth() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return findMemberByEmail(email);
    }
}
