package com.efub.bageasy.domain.image.service;

import com.efub.bageasy.domain.image.domain.Image;
import com.efub.bageasy.domain.image.repository.ImageRepository;
import com.efub.bageasy.domain.post.domain.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    public List<Image> findPostImage(Post post) {
        return imageRepository.findALLByPost(post);
    }

    public List<Image> findImageList(List<Long> imageId) {
        List<Image> imageList = new ArrayList<>();
        for(Long imgId: imageId){
            imageList.add(findImage(imgId));
        }
        return imageList;
    }

    public Image findImage(Long imageId) {
        return imageRepository.findImageByImageId(imageId);
    }

    public Image findImageByUrl(String imageUrl){
        return imageRepository.findImageByImageUrl(imageUrl);
    }
}
