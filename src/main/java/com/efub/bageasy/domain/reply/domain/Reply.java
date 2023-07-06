package com.efub.bageasy.domain.reply.domain;

import com.efub.bageasy.global.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;

@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Reply extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long replyId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "is_secret", nullable = false)
    private Boolean isSecret;

    @Column(nullable = false)
    private String content;

    @Column(name = "comment_id", nullable = false)
    private Long commentId;
}

