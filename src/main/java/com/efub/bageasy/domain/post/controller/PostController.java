package com.efub.bageasy.domain.post.controller;

import com.efub.bageasy.domain.image.domain.Image;
import com.efub.bageasy.domain.image.service.ImageService;
import com.efub.bageasy.domain.member.domain.Member;
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

    //게시글 생성
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public PostResponseDto createPost(
            @AuthUser Member member,
            @RequestPart(value="dto") PostRequestDto requestDto,
            @RequestPart(value="image") List<MultipartFile> images) throws IOException {

        if(images == null){
            throw new IOException("이미지가 없습니다.");
        }

        List<String> imgPaths = s3Service.upload(images);

        Post post = postService.addPost(member,requestDto,imgPaths);

        return new PostResponseDto(post);
    }


    // 양도글 수정
    @PutMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public String modifyPost(@AuthUser Member member,
                             @PathVariable Long postId,
                             @RequestPart(value="dto") PostUpdateRequestDto requestDto,
                             @RequestPart(value="addImage") List<MultipartFile> addImages) throws IOException {

        List<Image> deleteImageList = imageService.findImageList(requestDto.getImageIdList()); // 삭제할 이미지

        List<String> deleteImageUrlList = new ArrayList<>();
        for(Image image:deleteImageList){
            deleteImageUrlList.add(image.getImageUrl());
            s3Service.deleteImage(image.getImageUrl());
        }

        List<String> imgPaths = s3Service.upload(addImages); // S3 에 추가 이미지 업로드

        postService.updatePost(postId,requestDto,deleteImageList,imgPaths);


        Post post=postService.findPost(postId);
        return requestDto.getImageIdList()+" , "+deleteImageUrlList.get(0).substring(deleteImageUrlList.get(0).lastIndexOf('/') + 1,
                deleteImageUrlList.get(0).length());

    }


    // 구매 확정
    @PutMapping("/{postId}/isSold")
    @ResponseStatus(HttpStatus.OK)
    public PostListResponseDto updateIsSold(@AuthUser Member member,
                                            @PathVariable Long postId,
                                            @RequestBody @Valid PostUpdateIsSoldRequestDto requestDto){

        postService.updateIsSold(requestDto,postId,member);
        Post post = postService.findPost(postId);
        List<Image>imageList = imageService.findPostImage(post);

        PostListResponseDto responseDto = new PostListResponseDto(post,imageList);
        return responseDto;

    }


    // 전체 양도글 리스트 조회
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<PostListResponseDto> getPostList(){
        List<Post> posts = postService.findPostList();
        List<PostListResponseDto> responseDtoList = new ArrayList<>();

        for(Post post:posts){
            List<Image> images = imageService.findPostImage(post);

            responseDtoList.add(new PostListResponseDto(post,images));
        }
        return responseDtoList;
    }

    // 양도글 조회 : 1개
    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public PostListResponseDto getPost(@PathVariable Long postId){
        Post post = postService.findPost(postId);
        List<Image> images = imageService.findPostImage(post);

        PostListResponseDto responseDto = new PostListResponseDto(post,images);
        return responseDto;

    }

    //양도글 삭제
    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public String deletePost(@AuthUser Member member, @PathVariable Long postId) throws IOException {
        Post post = postService.findPost(postId);
        List<Image> imageList = imageService.findPostImage(post);

        for(Image image:imageList){
            s3Service.deleteImage(image.getImageUrl()); // 이미지 S3 삭제
        }

        postService.deletePost(member,postId); // 게시글 삭제

        return "성공적으로 삭제되었습니다!";
    }
}
