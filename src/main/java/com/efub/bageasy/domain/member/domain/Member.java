package com.efub.bageasy.domain.member.domain;

import com.efub.bageasy.global.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long memberId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String school;

    @Builder
    public Member(String email, String nickname, String school){
        this.email = email;
        this.nickname = nickname;
        this.school = school;
    }

    public Member updateNickname(String nickname){
        this.nickname = nickname;
        return this;
    }

    public Member updateSchool(String school){
        this.school = school;
        return this;
    }
}
