package com.efub.bageasy.domain.member.dto.response;

import com.efub.bageasy.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponseDto {

    // DB에 있는 멤버인지
    private Boolean isExistingMember;
    private Long memberId;
    private String email;
    private String nickname;
    private String accessToken;
    //private String refreshToken;

    @Builder
    public LoginResponseDto(Member member, String accessToken, Boolean isExistingMember){
        this.isExistingMember = isExistingMember;
        this.memberId = member.getMemberId();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.accessToken = accessToken;
    }
}
