package com.efub.bageasy.domain.heart.controller;

import com.efub.bageasy.domain.heart.dto.HeartPostResponseDto;
import com.efub.bageasy.domain.heart.service.HeartService;
import com.efub.bageasy.domain.member.domain.Member;
import com.efub.bageasy.global.config.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts/likes")
@RequiredArgsConstructor
public class HeartPostController {
    private HeartService heartService;

    @GetMapping("")
    public ResponseEntity<List<HeartPostResponseDto>> checkPostHeart(@AuthUser Member member) {
        List<HeartPostResponseDto> heartPostsResponseDto = heartService.findHeartPost(member);
        return ResponseEntity.status(HttpStatus.OK).body(heartPostsResponseDto);
    }
}

