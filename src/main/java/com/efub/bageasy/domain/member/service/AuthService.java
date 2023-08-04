package com.efub.bageasy.domain.member.service;

import com.efub.bageasy.domain.member.domain.Member;
import com.efub.bageasy.domain.member.dto.response.LoginResponseDto;
import com.efub.bageasy.domain.member.dto.response.ReissueResponseDto;
import com.efub.bageasy.domain.member.dto.response.TokenDto;
import com.efub.bageasy.domain.member.oauth.GoogleOAuthToken;
import com.efub.bageasy.domain.member.oauth.GoogleUser;
import com.efub.bageasy.global.exception.CustomException;
import com.efub.bageasy.global.exception.ErrorCode;
import com.efub.bageasy.global.service.RedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final RedisService redisService;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String GOOGLE_SNS_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String GOOGLE_SNS_CALLBACK_URL;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String GOOGLE_SNS_CLIENT_SECRET;

    @Value("${spring.OAuth2.google.url.token}")
    private String GOOGLE_TOKEN_REQUEST_URL;

    @Value("${spring.OAuth2.google.url.profile}")
    private String GOOGLE_USERINFO_REQUEST_URL;


    public LoginResponseDto googleLogin(String code) throws IOException {
        //구글로 일회성 코드를 보내 액세스 토큰이 담긴 응답객체를 받아옴
        //응답 객체가 JSON형식으로 되어 있으므로, 이를 deserialization해서 자바 객체에 담을 것이다.
        GoogleOAuthToken oAuthToken = getAccessToken(code);

        //액세스 토큰을 다시 구글로 보내 구글에 저장된 사용자 정보가 담긴 응답 객체를 받아온다.
        //다시 JSON 형식의 응답 객체를 자바 객체로 역직렬화한다.
        GoogleUser googleUser = getUserInfo(oAuthToken);

        String email = googleUser.getEmail();
        Boolean isExistingMember = memberService.checkJoined(email);

        Member member;
        if (!isExistingMember) {  //가입 처리
            member = memberService.saveMember(googleUser);
        } else {
            member = memberService.findMemberByEmail(email);
        }

        //앞으로 회원 인가 처리를 위한 jwtToken을 발급한다.
        TokenDto tokenDto = jwtTokenProvider.createToken(member.getEmail());

        //refresh token을 redis에 저장
        redisService.setValues(member.getEmail(),
                tokenDto.getRefreshToken(),
                jwtTokenProvider.getTokenExpirationTime(tokenDto.getRefreshToken()));

        return new LoginResponseDto(member, tokenDto.getAccessToken(), isExistingMember);
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

        ObjectMapper objectMapper = new ObjectMapper();
        GoogleUser googleUser = objectMapper.readValue(response.getBody(), GoogleUser.class);
        return googleUser;
    }

    // 토큰 재발급
    public ReissueResponseDto reissue(HttpServletRequest request) {

        // 1. Access Token 에서 User email 을 가져오기
        String accessToken = jwtTokenProvider.resolveToken(request);
        // 만료된 Access Token을 디코딩하여 Payload 값을 가져옴
        HashMap<String, String> payloadMap = getPayloadByToken(accessToken);
        String email = payloadMap.get("sub");

        // 2. Redis 에서 User email 을 기반으로 저장된 Refresh Token 값을 가져오기
        String refreshToken = redisService.getValues(email);

        // 3. Refresh Token 검증
        // Redis에 없거나 만료되었으면 다시 로그인하기
        if (refreshToken == null)
            throw new CustomException(ErrorCode.NON_LOGIN);
        if (!jwtTokenProvider.validateToken(refreshToken))
            throw new CustomException(ErrorCode.NON_LOGIN);

        // 4. email로 회원 찾기
        Member member = memberService.findMemberByEmail(email);

        // 5. 새로운 토큰 생성
        TokenDto newToken = jwtTokenProvider.createToken(email);

        // 6. Refresh Token Redis 업데이트
        redisService.setValues(email, newToken.getRefreshToken(), jwtTokenProvider.getTokenExpirationTime(newToken.getRefreshToken()));

        return new ReissueResponseDto(newToken.getAccessToken(), member);
    }

    public HashMap<String, String> getPayloadByToken(String token) {
        try {
            String[] splitJwt = token.split("\\.");

            Base64.Decoder decoder = Base64.getDecoder();
            String payload = new String(decoder.decode(splitJwt[1].getBytes()));

            return new ObjectMapper().readValue(payload, HashMap.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
