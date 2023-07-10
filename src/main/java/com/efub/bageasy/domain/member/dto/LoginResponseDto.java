package com.efub.bageasy.domain.member.dto;

import com.efub.bageasy.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

//클라이언트로 보낼 member 정보, accessToken 등이 담긴 객체
@Getter
public class LoginResponseDto {

    private Long memberId;
    private String email;
    private String nickname;
    private String accessToken;
    //private String refreshToken;

    @Builder
    public LoginResponseDto(Member member, String accessToken){
        this.memberId = member.getMemberId();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.accessToken = accessToken;
    }
}
