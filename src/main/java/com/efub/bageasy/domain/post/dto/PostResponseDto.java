package com.efub.bageasy.domain.post.dto;

import com.efub.bageasy.domain.post.domain.Post;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostResponseDto {
    private Long sellerId;
    private String postTitle;
    private String postContent;
    private Long price;
    private Boolean isSold;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public PostResponseDto(Post post) {
        this.sellerId=post.getMemberId();
        this.postTitle=post.getTitle();
        this.postContent=post.getContent();
        this.price=post.getPrice();
        this.isSold=post.getIsSold();
        this.createdAt=post.getCreatedAt();
        this.modifiedAt=post.getModifiedAt();

    }
}
