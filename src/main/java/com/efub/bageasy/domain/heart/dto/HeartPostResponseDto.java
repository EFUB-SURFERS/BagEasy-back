package com.efub.bageasy.domain.heart.dto;

import com.efub.bageasy.domain.post.domain.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class HeartPostResponseDto {
    private Long postId;
    private String postTitle;
    private String postContent;
    private Long price;
    private Boolean isSold;
    private String school;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String imageUrl;

    public HeartPostResponseDto(Post post){
        this.postId=post.getPostId();
        this.postTitle=post.getTitle();
        this.postContent=post.getContent();
        this.price=post.getPrice();
        this.isSold=post.getIsSold();
        this.school=post.getSchool();
        this.createdAt=post.getCreatedAt();
        this.modifiedAt=post.getModifiedAt();
        this.imageUrl = post.getImageList().get(0).getImageUrl();
    }
}
