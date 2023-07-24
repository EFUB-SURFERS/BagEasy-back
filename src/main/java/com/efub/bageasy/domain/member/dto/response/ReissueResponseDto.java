package com.efub.bageasy.domain.member.dto.response;

import com.efub.bageasy.domain.member.domain.Member;
import lombok.Getter;

@Getter
public class ReissueResponseDto {
    private String accessToken;
    private Long memberId;
    private String email;
    private String nickname;

    public ReissueResponseDto(String accessToken, Member member){
        this.accessToken = accessToken;
        this.memberId = member.getMemberId();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
    }
}
