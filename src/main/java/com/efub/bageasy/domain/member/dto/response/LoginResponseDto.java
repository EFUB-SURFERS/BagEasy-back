package com.efub.bageasy.domain.member.dto.response;

import com.efub.bageasy.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponseDto {

    // DB에 있는 멤버인지
    private Boolean isExistingMember;
    private String email;
    private String nickname;
    private String accessToken;

    @Builder
    public LoginResponseDto(Member member, String accessToken, Boolean isExistingMember){
        this.isExistingMember = isExistingMember;
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.accessToken = accessToken;
    }
}
