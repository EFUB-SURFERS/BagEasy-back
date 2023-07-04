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
    private Long replyId;

    @Column(nullable = false)
    private Boolean isSecret;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Long commentId;
}

