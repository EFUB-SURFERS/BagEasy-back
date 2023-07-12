package com.efub.bageasy.domain.member.domain;

import com.efub.bageasy.global.BaseTimeEntity;
import lombok.*;

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

    @Builder
    public Member(String email, String nickname, Long schoolId){
        this.email = email;
        this.nickname = nickname;
        this.schoolId = schoolId;
    }
}
