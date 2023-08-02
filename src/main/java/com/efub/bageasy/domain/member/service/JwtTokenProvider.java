package com.efub.bageasy.domain.member.service;


import com.efub.bageasy.domain.member.domain.Member;
import com.efub.bageasy.domain.member.dto.response.TokenDto;
import com.efub.bageasy.domain.member.repository.MemberRepository;
import com.efub.bageasy.global.exception.CustomException;
import com.efub.bageasy.global.exception.ErrorCode;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;


@Service
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    private final MemberRepository memberRepository;

    @Value("${jwt.token.key}")
    private String SECRET_KEY;

    private Long accessTokenValidTime = 1000L * 60;  // 1분
    private long refreshTokenValidTime = 1000L * 60 * 60 * 24 * 30; // 30일

    @PostConstruct
    protected void init() {
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
    }

    //JWT 토큰 생성
    public TokenDto createToken(String email) {

        Date now = new Date();
        Claims claims = Jwts.claims()
                .setSubject(email);

        String accessToken =  Jwts.builder()
                .setClaims(claims) // 페이로드
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        String refreshToken = Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        return new TokenDto(accessToken, refreshToken);
    }

    //JWT 토큰에서 인증정보 조회
    public Authentication getAuthentication(String token) {
        try{
            String email = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
            Member member = memberRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.NO_MEMBER_EXIST));
            return new UsernamePasswordAuthenticationToken(member, "");
        }catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.EXPIRED_TOKEN);
        } catch (JwtException e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.NON_LOGIN);
        }
    }

    // Request의 Header에서 token 값을 가져오기
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    // 토큰의 유효성 + 만료일자 확인  // -> 토큰이 expire되지 않았는지 True/False로 반환해줌.
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            log.info(ErrorCode.EXPIRED_TOKEN.getMessage());
        } catch (JwtException e) {
            log.info(ErrorCode.INVALID_TOKEN.getMessage());
        } catch (IllegalArgumentException e) {
            log.info(ErrorCode.NON_LOGIN.getMessage());
        }
        return false;
    }

    public Long getTokenExpirationTime(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token)
                .getBody().getExpiration().getTime();
    }


    public String getNicknameFromToken(String accessToken) {
        String email =  Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(accessToken).getBody().getSubject();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new CustomException(ErrorCode.NO_MEMBER_EXIST));
        String nickname = member.getNickname();
        return nickname;
    }
}