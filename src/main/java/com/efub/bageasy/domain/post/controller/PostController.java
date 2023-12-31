package com.efub.bageasy.domain.post.controller;

import com.efub.bageasy.domain.image.domain.Image;
import com.efub.bageasy.domain.image.service.ImageService;
import com.efub.bageasy.domain.member.domain.Member;
import com.efub.bageasy.domain.member.service.MemberService;
import com.efub.bageasy.domain.post.domain.Post;
import com.efub.bageasy.domain.post.dto.*;
import com.efub.bageasy.domain.post.service.PostService;
import com.efub.bageasy.global.config.AuthUser;
import com.efub.bageasy.global.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final S3Service s3Service;
    private final ImageService imageService;
    private final MemberService memberService;


    //게시글 생성
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public PostResponseDto createPost(
            @AuthUser Member member,
            @RequestPart(value="dto") PostRequestDto requestDto,
            @RequestPart(value="image") List<MultipartFile> images) throws IOException {

        return postService.addPost(member,requestDto,images);
    }


    // 게시글 수정
    @PutMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public PostResponseDto modifyPost(@AuthUser Member member,
                                         @PathVariable Long postId,
                                         @RequestPart(value="dto") PostUpdateRequestDto requestDto,
                                         @RequestPart(value="addImage" , required = false) List<MultipartFile> addImages) throws IOException {

        return postService.updatePost(member,postId,requestDto,addImages);

    }


    // 구매 확정
    @PutMapping("/{postId}/isSold")
    @ResponseStatus(HttpStatus.OK)
    public PostResponseDto updateIsSold(@AuthUser Member member,
                                        @PathVariable Long postId,
                                        @RequestBody @Valid PostUpdateIsSoldRequestDto requestDto){
        return postService.updateIsSold(requestDto,postId,member);

    }

    // 전체 게시글 리스트 조회
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<PostResponseDto> getPostList(){
        List<Post> posts = postService.findPostList();
        List<PostResponseDto> responseDtoList = new ArrayList<>();

        for(Post post:posts){
            Member member = memberService.findMemberById(post.getMemberId());
            String buyerNickname = null;
            Long heartCount = postService.countHeart(post.getPostId());
            if(post.getBuyerId() != null){
                buyerNickname = memberService.findNicknameById(post.getBuyerId());
            }

            List<Image> images = imageService.findPostImage(post);
            responseDtoList.add(new PostResponseDto(post, images, member, buyerNickname, heartCount));
        }

        return responseDtoList;
    }


    //학교로 양도글 리스트 조회
    @PostMapping("/school")
    @ResponseStatus(HttpStatus.OK)
    public List<PostResponseDto> getPostListBySchool(@RequestBody PostSchoolRequestDto requestDto){
        return postService.findPostListBySchool(requestDto.getSchoolName());
    }

    //학교로 양도글 리스트 조회 - 판매중인 양도글만 조회
    @PostMapping("/school/sales")
    @ResponseStatus(HttpStatus.OK)
    public List<PostResponseDto> getPostListBySchoolNotSold(@RequestBody PostSchoolRequestDto requestDto){
        List<PostResponseDto> responseDtoListBySchool = postService.findPostListBySchool(requestDto.getSchoolName());
        List<PostResponseDto> responseDtoList = new ArrayList<>();
        for(PostResponseDto responseDto : responseDtoListBySchool){
            if(responseDto.getIsSold() == false){
                responseDtoList.add(responseDto);
            }
        }
        return responseDtoList;
    }


    //판매중인 게시글만 조회
    @GetMapping("/sales")
    @ResponseStatus(HttpStatus.OK)
    public List<PostResponseDto> getPostListNotSold(){
        return postService.findPostListNotSold();
    }


    // 게시글 조회 : 1개
    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public PostResponseDto getPost(@PathVariable Long postId){
        Post post = postService.findPost(postId);
        Member member = memberService.findMemberById(post.getMemberId());

        String buyerNickName = null;
        if(post.getBuyerId() != null) buyerNickName = memberService.findNicknameById(post.getBuyerId());

        List<Image> images = imageService.findPostImage(post);
        Long heartCount = postService.countHeart(post.getPostId());

        PostResponseDto responseDto = new PostResponseDto(post, images, member, buyerNickName, heartCount);
        return responseDto;

    }


    //게시글 삭제
    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public String deletePost(@AuthUser Member member, @PathVariable Long postId) throws IOException {
        postService.deletePost(member,postId); // 게시글 삭제
        return "성공적으로 삭제되었습니다!";
    }
}
