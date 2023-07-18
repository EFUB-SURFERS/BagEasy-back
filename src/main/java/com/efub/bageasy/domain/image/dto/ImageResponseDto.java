package com.efub.bageasy.domain.image.dto;

import com.efub.bageasy.domain.image.domain.Image;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ImageResponseDto {
    private Long imageId;
    private String imageUrl;
    private Long postId;

    public ImageResponseDto(Image image){
        this.imageId=image.getImageId();
        this.imageUrl=image.getImageUrl();
        this.postId=image.getPost().getPostId();
    }
}
