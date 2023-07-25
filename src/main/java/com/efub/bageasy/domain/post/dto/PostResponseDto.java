package com.efub.bageasy.domain.post.dto;

import com.efub.bageasy.domain.image.domain.Image;
import com.efub.bageasy.domain.image.dto.ImageResponseDto;
import com.efub.bageasy.domain.member.domain.Member;
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
public class PostResponseDto {
    private Long postId;
    private Long sellerId;
    private String sellerNickname;
    private String postTitle;
    private String postContent;
    private Long price;
    private Boolean isSold;
    private String school;
    private String buyerNickname = null;
    private Long heartCount;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private List<ImageResponseDto> imageResponseDtos = new ArrayList<>();

    public PostResponseDto(Post post, List<Image> images, Member member){

        this.postId=post.getPostId();
        this.sellerId= member.getMemberId();
        this.sellerNickname = member.getNickname();
        this.postTitle=post.getTitle();
        this.postContent=post.getContent();
        this.price=post.getPrice();
        this.isSold=post.getIsSold();
        this.school=post.getSchool();
        this.createdAt=post.getCreatedAt();
        this.modifiedAt=post.getModifiedAt();
        for(Image image:images){
            imageResponseDtos.add(new ImageResponseDto(image));
        }
    }

    public PostResponseDto(Post post, List<Image> images, Member member, String buyerNickname, Long heartCount){
        this.postId=post.getPostId();
        this.sellerId = member.getMemberId();
        this.sellerNickname = member.getNickname();
        this.postTitle=post.getTitle();
        this.postContent=post.getContent();
        this.price=post.getPrice();
        this.isSold=post.getIsSold();
        this.school=post.getSchool();
        this.buyerNickname = buyerNickname;
        this.heartCount = heartCount;
        this.createdAt=post.getCreatedAt();
        this.modifiedAt=post.getModifiedAt();
        for(Image image:images){
            imageResponseDtos.add(new ImageResponseDto(image));
        }
    }
}
