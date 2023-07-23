package com.efub.bageasy.domain.heart.controller;

import com.efub.bageasy.domain.heart.dto.HeartResponseDto;
import com.efub.bageasy.domain.heart.service.HeartService;
import com.efub.bageasy.domain.member.domain.Member;
import com.efub.bageasy.global.config.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts/{postId}/likes")
@RequiredArgsConstructor
public class HeartController {
    private HeartService heartService;

    @PostMapping("")
    public ResponseEntity<String> postHeart(@AuthUser Member member, @PathVariable("postId") Long postId) {
        heartService.createByPostId(member, postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteHeart(@AuthUser Member member, @PathVariable("postId") Long postId) {
        heartService.deleteByPostId(member, postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<HeartResponseDto> checkPostHeart(@AuthUser Member member, @PathVariable("postId") Long postId) {
        HeartResponseDto heartResponseDto = heartService.findExistence(member, postId);
        return ResponseEntity.status(HttpStatus.OK).body(heartResponseDto);
    }
}
