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

    @Column(name = "school_id")
    private Long schoolId;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(String email, String nickname, Long schoolId, Role role){
        this.email = email;
        this.nickname = nickname;
        this.schoolId = schoolId;
        this.role = role;
    }

    public Member updateNickname(String nickname){
        this.nickname = nickname;
        return this;
    }
}
