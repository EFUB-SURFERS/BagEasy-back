package com.efub.bageasy.domain.post.domain;

import com.efub.bageasy.global.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;

@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long postId;

    @Column(nullable = false)
    private String title;

    @Column
    private String content;

    @Column(name = "is_sold", nullable = false)
    private Boolean isSold;

    @Column
    private Long price;

    @Column(name = "school_id", nullable = false)
    private Long schoolId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "buyer_id")
    private Long buyerId;
}
