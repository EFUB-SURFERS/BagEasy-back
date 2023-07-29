package com.efub.bageasy.domain.member.dto.response;

import com.efub.bageasy.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberInfoDto {

    public String email;
    public String nickname;
    public String school;

    @Builder
    public MemberInfoDto(Member member){
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.school = member.getSchool();
    }
}

