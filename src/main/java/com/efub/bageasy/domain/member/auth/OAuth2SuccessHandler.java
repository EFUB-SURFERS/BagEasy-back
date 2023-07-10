package com.efub.bageasy.domain.member.auth;

import com.efub.bageasy.domain.member.domain.Member;
import com.efub.bageasy.domain.member.domain.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        Member member = principal.getMember();

        String email = member.getEmail();
        Role role = member.getRole();

        String additionalInputUri = "";

        // access token 생성
        String accessToken = jwtTokenProvider.createToken(email, role);

        String redirectUrl = makeRedirectUrl(additionalInputUri, principal, accessToken);
        log.debug("accessToken={}", accessToken);
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    private String makeRedirectUrl(String uri, UserPrincipal principal, String accessToken) {

        Member member = principal.getMember();

        return UriComponentsBuilder.fromUriString("http://localhost:3000/" + uri)
                .queryParam("memberId", member.getMemberId())
                .queryParam("accessToken", accessToken)
                .build().toUriString();
    }
}
