package com.efub.bageasy.domain.comment.domain;

import com.efub.bageasy.global.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long commentId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "is_secret", nullable = false)
    private Boolean isSecret;

    @Column(nullable = false)
    private String content;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    public Comment(Long memberId, Boolean isSecret, String  content, Long postId){
        this.memberId=memberId;
        this.isSecret = isSecret;
        this.content=content;
        this.postId=postId;
    }
}
