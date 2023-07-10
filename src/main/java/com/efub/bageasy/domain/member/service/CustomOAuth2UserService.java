package com.efub.bageasy.domain.member.service;

import com.efub.bageasy.domain.member.auth.UserPrincipal;
import com.efub.bageasy.domain.member.domain.Member;
import com.efub.bageasy.domain.member.domain.Role;
import com.efub.bageasy.domain.member.dto.request.OAuthUserDto;
import com.efub.bageasy.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(oAuth2UserRequest);

        return processOAuth2User(oAuth2UserRequest, oAuth2User);
    }

    protected OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {

        String userNameAttributeName = oAuth2UserRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        OAuthUserDto oAuthUserDto = OAuthUserDto.ofGoogle(userNameAttributeName, oAuth2User.getAttributes());

        if (!StringUtils.hasText(oAuthUserDto.getEmail())) {
            throw new RuntimeException("Email not found from Google");
        }

        Member member = memberRepository.findByEmail(oAuthUserDto.getEmail()).orElse(null);

        //가입되지 않은 경우
        if (member == null) {
            member = registerUser(oAuthUserDto);
        }

        return new UserPrincipal(member, oAuthUserDto.getAttributes());
    }

    private Member registerUser(OAuthUserDto oAuthUserDto) {
        Member member = Member.builder()
                .email(oAuthUserDto.getEmail())
                .nickname(oAuthUserDto.getName())
                .role(Role.ROLE_USER)
                .build();

        return memberRepository.save(member);
    }
}
