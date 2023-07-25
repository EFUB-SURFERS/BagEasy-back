package com.efub.bageasy.domain.member.controller;

import com.efub.bageasy.domain.image.domain.Image;
import com.efub.bageasy.domain.image.service.ImageService;
import com.efub.bageasy.domain.member.domain.Member;
import com.efub.bageasy.domain.member.service.MemberService;
import com.efub.bageasy.domain.post.domain.Post;
import com.efub.bageasy.domain.post.dto.PostResponseDto;
import com.efub.bageasy.domain.post.service.PostService;
import com.efub.bageasy.global.config.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/members/posts")
@RequiredArgsConstructor
public class MemberPostController {

    private final MemberService memberService;
    private final PostService postService;
    private final ImageService imageService;

    // 멤버 당 작성한 양도글 목록
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PostResponseDto> getMemberPost(@AuthUser Member member) {
        List<Post> postList = postService.findPostListBySellerId(member.getMemberId());

        return postListToDtoList(postList, member);
    }

    // 구매내역
    @GetMapping("/purchases")
    @ResponseStatus(HttpStatus.OK)
    public List<PostResponseDto> purchaseList(@AuthUser Member member) {
        List<Post> purchases = postService.findPostListByBuyerId(member.getMemberId());

        return postListToDtoList(purchases, member);
    }

    // 판매내역
    @GetMapping("/sales")
    @ResponseStatus(HttpStatus.OK)
    public List<PostResponseDto> saleList(@AuthUser Member member) {
        List<Post> postList = postService.findPostListBySellerId(member.getMemberId());

        List<Post> sales = new ArrayList<>();
        for (Post post : postList) {
            if (post.getIsSold()) sales.add(post);
        }

        return postListToDtoList(sales, member);
    }

    public List<PostResponseDto> postListToDtoList(List<Post> postList, Member member) {
        List<PostResponseDto> dtoList = new ArrayList<>();

        for (Post post : postList) {

            String buyerNickName = null;
            if(post.getBuyerId() != null) buyerNickName = memberService.findNicknameById(post.getBuyerId());
            List<Image> images = imageService.findPostImage(post);
            Long heartCount = postService.countHeart(post.getPostId());

            dtoList.add(new PostResponseDto(post, images, member, buyerNickName, heartCount));
        }

        return dtoList;
    }
}
