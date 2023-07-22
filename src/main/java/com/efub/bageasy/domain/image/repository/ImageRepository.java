package com.efub.bageasy.domain.image.repository;

import com.efub.bageasy.domain.image.domain.Image;
import com.efub.bageasy.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image,Long> {

    List<Image> findALLByPost(Post post);
    Image findImageByImageId(Long imageId);

    // url 로 이미지 찾기
    Image findImageByImageUrl(String imageUrl);

}
