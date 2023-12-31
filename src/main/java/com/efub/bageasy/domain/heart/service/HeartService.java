package com.efub.bageasy.domain.heart.service;

import com.efub.bageasy.domain.heart.domain.Heart;
import com.efub.bageasy.domain.heart.dto.HeartResponseDto;
import com.efub.bageasy.domain.heart.repository.HeartRepository;
import com.efub.bageasy.domain.image.service.ImageService;
import com.efub.bageasy.domain.member.domain.Member;
import com.efub.bageasy.domain.member.service.MemberService;
import com.efub.bageasy.domain.post.dto.PostResponseDto;
import com.efub.bageasy.domain.post.repository.PostRepository;
import com.efub.bageasy.domain.post.service.PostService;
import com.efub.bageasy.global.exception.CustomException;
import com.efub.bageasy.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HeartService {
    private final HeartRepository heartRepository;
    private final PostRepository postRepository;

    private final ImageService imageService;
    private final PostService postService;
    private final MemberService memberService;

    @Transactional
    public void createByPostId(Member member, Long postId){
        postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        if(heartRepository.existsHeartByMemberIdAndPostId(member.getMemberId(), postId)){
            throw new CustomException(ErrorCode.ALREADY_LIKED);
        }
        Heart heart = new Heart(member.getMemberId(), postId);
        heartRepository.save(heart);
    }

    @Transactional
    public void deleteByPostId(Member member, Long postId){
        postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        if(!heartRepository.existsHeartByMemberIdAndPostId(member.getMemberId(), postId)){
            throw new CustomException(ErrorCode.NOT_LIKED);
        }
        Heart heart = heartRepository.findByMemberIdAndPostId(member.getMemberId(), postId)
                .orElseThrow(() -> new CustomException(ErrorCode.HEART_NOT_FOUND));
        heartRepository.delete(heart);
    }

    @Transactional(readOnly = true)
    public HeartResponseDto findExistence(Member member, Long postId){
        postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        if(heartRepository.existsHeartByMemberIdAndPostId(member.getMemberId(), postId)){
            return new HeartResponseDto(true);
        }
        else{
            return new HeartResponseDto(false);
        }
    }

    //찜한 양도글 목록 조회
    @Transactional(readOnly = true)
    public List<PostResponseDto> findHeartPost(Member member){
        return heartRepository.findByMemberId(member.getMemberId())
                .stream()
                .map(heart -> postRepository.findById(heart.getPostId())
                        .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND)))
                .map(post -> {
                    if(post.getBuyerId() == null){
                        return new PostResponseDto(post, imageService.findPostImage(post), member,
                                null, postService.countHeart(post.getPostId()));
                    }
                    else{
                        return new PostResponseDto(post, imageService.findPostImage(post), member,
                                memberService.findNicknameById(post.getBuyerId()), postService.countHeart(post.getPostId()));
                    }
                })
                .collect(Collectors.toList());
    }
}
