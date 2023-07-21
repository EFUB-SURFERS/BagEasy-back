package com.efub.bageasy.domain.post.dto;

import com.efub.bageasy.domain.member.domain.Member;
import com.efub.bageasy.domain.post.domain.Post;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostResponseDtoval {
    private String sellerNickname;
    private String postTitle;
    private String postContent;
    private Long price;
    private Boolean isSold;
    private String school;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public PostResponseDtoval(Post post, Member member) {
        this.sellerNickname = member.getNickname();
        this.postTitle=post.getTitle();
        this.postContent=post.getContent();
        this.price=post.getPrice();
        this.isSold=post.getIsSold();
        this.school=post.getSchool();
        this.createdAt=post.getCreatedAt();
        this.modifiedAt=post.getModifiedAt();

    }
}
