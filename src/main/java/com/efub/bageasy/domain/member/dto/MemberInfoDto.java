package com.efub.bageasy.domain.member.dto;

import com.efub.bageasy.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberInfoDto {
    public Long memberId;
    public String email;
    public String nickname;
    public Long schoolId;

    @Builder
    public MemberInfoDto(Member member){
        this.memberId = member.getMemberId();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.schoolId = member.getSchoolId();
    }
}

