package com.efub.bageasy.domain.post.service;

import com.efub.bageasy.domain.heart.domain.Heart;
import com.efub.bageasy.domain.heart.repository.HeartRepository;
import com.efub.bageasy.domain.image.domain.Image;
import com.efub.bageasy.domain.image.repository.ImageRepository;
//import com.efub.bageasy.domain.member.repository.MemberRepository;
import com.efub.bageasy.domain.member.domain.Member;
import com.efub.bageasy.domain.post.domain.Post;
import com.efub.bageasy.domain.post.dto.PostRequestDto;
import com.efub.bageasy.domain.post.dto.PostUpdateIsSoldRequestDto;
import com.efub.bageasy.domain.post.dto.PostUpdateRequestDto;
import com.efub.bageasy.domain.post.repository.PostRepository;
import com.efub.bageasy.global.exception.CustomException;
import com.efub.bageasy.global.exception.ErrorCode;
import com.efub.bageasy.global.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final HeartRepository heartRepository;

    @Autowired
    private final S3Service s3Service;

    @Transactional
    public Post addPost(Member member, PostRequestDto requestDto , List<String> imgPaths) throws IOException {
        String title = requestDto.getPostTitle();
        String content = requestDto.getPostContent();
        Long price = requestDto.getPrice();
        Long memberId=member.getMemberId();
        String school = requestDto.getSchool();

        Post post = new Post(title,content,price,memberId,school);
        postRepository.save(post);

        List<String> imgList = new ArrayList<>();
        for(String imgUrl : imgPaths){
            Image image = new Image(imgUrl,post);
            imageRepository.save(image);
            imgList.add(image.getImageUrl());
        }

        return post;
    }

    public List<Post> findPostList() {
        return postRepository.findAll();
    }


    @Transactional
    // 양도글 내용 수정
    public Post updatePost(Long postId, PostUpdateRequestDto requestDto, List<Image> deleteImageList, List<String> imgPaths) {
        Post post=postRepository.findById(postId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 양도글입니다."));

        // 이미지 삭제
        for(Image image:deleteImageList){
            imageRepository.delete(image);
        }

        // 이미지 추가
        List<String> imgList = new ArrayList<>();

        for(String imgUrl : imgPaths){
            Image image = new Image(imgUrl,post);
            imageRepository.save(image);
            imgList.add(image.getImageUrl());
        }

        post.update(requestDto);
        return post;
    }

    // 양도글 조회 : 1개 조회
    public Post findPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 게시글입니다."));
        return post;
    }

    // 멤버 Id 로 양도글 목록 조회
    public List<Post> findPostListBySellerId(Long memberId) {
        return postRepository.findAllByMemberId(memberId);
    }

    public List<Post> findPostListByBuyerId(Long buyerId){
        return postRepository.findAllByBuyerId(buyerId);
    }

    // 구매 확정
    public void updateIsSold(PostUpdateIsSoldRequestDto requestDto, Long postId, Member member) {
        Post post = findPost(postId);
        if(post.getMemberId()==member.getMemberId()){
            post.updateIsSold(requestDto.getBuyerId());
        }
    }

    public void deletePost(Member member, Long postId) {
        Post post=findPost(postId);
        postRepository.delete(post);
    }

    public Long countHeart(Long postId){
        return heartRepository.countByPostId(postId);
    }
}
