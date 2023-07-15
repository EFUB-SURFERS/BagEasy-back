package com.efub.bageasy.domain.member.controller;

import com.efub.bageasy.domain.image.domain.Image;
import com.efub.bageasy.domain.image.service.ImageService;
import com.efub.bageasy.domain.member.domain.Member;
import com.efub.bageasy.domain.member.service.MemberService;
import com.efub.bageasy.domain.post.domain.Post;
import com.efub.bageasy.domain.post.dto.PostListResponseDto;
import com.efub.bageasy.domain.post.service.PostService;
import com.efub.bageasy.global.config.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/members/{memberId}/posts")
@RequiredArgsConstructor
public class MemberPostController {
    private final MemberService memberService;
    private final PostService postService;
    private final ImageService imageService;

    // 멤버 당 작성한 양도글 목록
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PostListResponseDto> getMemberPost(@AuthUser Member member, @PathVariable Long memberId){
        //Member member = memberService.findMemberById(memberId);
        List<Post> postList = postService.findPostListBySellerId(member.getMemberId());

        List<PostListResponseDto> postListResponseDtoList = new ArrayList<>();

        for(Post post:postList){
            List<Image> images = imageService.findPostImage(post);

            postListResponseDtoList.add(new PostListResponseDto(post,images));
        }

        return postListResponseDtoList;
    }

}
