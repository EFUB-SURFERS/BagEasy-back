package com.efub.bageasy.domain.post.dto;

import com.efub.bageasy.domain.image.domain.Image;
import com.efub.bageasy.domain.image.dto.ImageResponseDto;
import com.efub.bageasy.domain.post.domain.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class PostListResponseDto {
    private Long postId;
    private Long sellerId;
    private String postTitle;
    private String postContent;
    private Long price;
    private Boolean isSold;
    private String school;
    private Long buyerId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private List<ImageResponseDto> imageResponseDtos = new ArrayList<>();

    public PostListResponseDto(Post post, List<Image> images){
        this.postId=post.getPostId();
        this.sellerId=post.getMemberId();
        this.postTitle=post.getTitle();
        this.postContent=post.getContent();
        this.price=post.getPrice();
        this.isSold=post.getIsSold();
        this.school=post.getSchool();
        this.buyerId=post.getBuyerId();
        this.createdAt=post.getCreatedAt();
        this.modifiedAt=post.getModifiedAt();

        for(Image image:images){
            imageResponseDtos.add(new ImageResponseDto(image));
        }

    }


}
