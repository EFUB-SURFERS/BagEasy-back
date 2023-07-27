package com.efub.bageasy.domain.post.service;

import com.efub.bageasy.domain.heart.domain.Heart;
import com.efub.bageasy.domain.heart.repository.HeartRepository;
import com.efub.bageasy.domain.image.domain.Image;
import com.efub.bageasy.domain.image.repository.ImageRepository;
//import com.efub.bageasy.domain.member.repository.MemberRepository;
import com.efub.bageasy.domain.image.service.ImageService;
import com.efub.bageasy.domain.member.domain.Member;
import com.efub.bageasy.domain.member.repository.MemberRepository;
import com.efub.bageasy.domain.member.service.MemberService;
import com.efub.bageasy.domain.post.domain.Post;
import com.efub.bageasy.domain.post.dto.PostRequestDto;
import com.efub.bageasy.domain.post.dto.PostResponseDto;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final ImageService imageService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
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

    @Transactional
    public PostResponseDto updatePost(Member member, Long postId, PostUpdateRequestDto requestDto, List<MultipartFile> addImages ) throws IOException {

        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new CustomException(ErrorCode.POST_NOT_EXIST));

        //양도글 작성자 외의 회원이 수정을 시도할 경우
        if(post.getBuyerId() != member.getMemberId()){
            throw new CustomException(ErrorCode.INVALID_MEMBER);
        }

        //이미지 수정이 있는 경우
        if(addImages != null){

            // 기존 이미지 삭제
            List<Image> deleteImageList = imageRepository.findALLByPost(post);
            for(Image deleteImage : deleteImageList){
                s3Service.deleteImage(deleteImage.getImageUrl());
                imageRepository.delete(deleteImage);
            }

            // 새로운 이미지 업로드
            List<String> imgPaths = s3Service.upload(addImages);
            for(String imgUrl : imgPaths){
                Image image = new Image(imgUrl,post);
                imageRepository.save(image);
            }
        }

        post.update(requestDto);

        List<Image> imageList = imageRepository.findALLByPost(post);
        String buyerNickName=null;
        if(post.getBuyerId() != null){
            buyerNickName = memberService.findNicknameById(post.getBuyerId());
        }
        Long heartCount = countHeart(post.getPostId());
        return new PostResponseDto(post, imageList, member, buyerNickName, heartCount);


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

    // 학교 이름으로 양도글 목록 조회
    public List<Post> findPostListBySchoolName(String school){
        return postRepository.findAllBySchool(school);
    }

    public List<Post> findPostListByBuyerId(Long buyerId){
        return postRepository.findAllByBuyerId(buyerId);
    }


    // 구매 확정
    public PostResponseDto updateIsSold(PostUpdateIsSoldRequestDto requestDto, Long postId, Member member) {
        Post post = findPost(postId);
        if(post.getMemberId() != member.getMemberId()){
            throw new CustomException(ErrorCode.INVALID_MEMBER);
        }
        else if(post.getMemberId()==member.getMemberId()){
           Long buyerId = memberService.findMemberByNickname(requestDto.getBuyerNickName()).getMemberId();
           post.updateIsSold(buyerId);
        }
        return makeDto(post);
    }


    // 게시글 삭제
    public void deletePost(Member member, Long postId) {
        Post post = findPost(postId);
        heartRepository.findByPostId(postId)
                        .forEach(heartRepository::delete);
        postRepository.delete(post);
    }


    public Long countHeart(Long postId){
        return heartRepository.countByPostId(postId);
    }


    //학교 이름으로 양도글 리스트 찾기
    public List<PostResponseDto> findPostListBySchool(String school) {
        List<Post> postList = findPostListBySchoolName(school);
        List<PostResponseDto> responseDtoList = new ArrayList<>();
        for(Post post : postList){
            Member member = memberService.findMemberById(post.getMemberId());
            List<Image> imageList = imageService.findPostImage(post);
            String buyerNickName = null;
            if(post.getBuyerId() != null) buyerNickName = memberService.findNicknameById(post.getBuyerId());
            Long heartCount = countHeart(post.getPostId());
            responseDtoList.add(new PostResponseDto(post,imageList,member,buyerNickName,heartCount));
        }
        return responseDtoList;
    }


    public List<PostResponseDto> findPostListNotSold() {
        List<Post> postList = findPostList();
        List<PostResponseDto> responseDtoList = new ArrayList<>();
        for(Post post:postList){
            if(post.getIsSold() == false){
                responseDtoList.add(makeDto(post));
            }
        }

        return responseDtoList;
    }

    public PostResponseDto makeDto(Post post){
        Member member = memberService.findMemberById(post.getMemberId());
        List<Image> imageList = imageService.findPostImage(post);
        String buyerNickName = null;
        if(post.getBuyerId() != null) buyerNickName = memberService.findNicknameById(post.getBuyerId());
        Long heartCount = countHeart(post.getPostId());
        return new PostResponseDto(post,imageList,member,buyerNickName,heartCount);
    }
}
