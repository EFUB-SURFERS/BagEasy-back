package com.efub.bageasy.domain.member.controller;

import com.efub.bageasy.domain.member.domain.Member;
import com.efub.bageasy.domain.member.dto.request.NicknameRequestDto;
import com.efub.bageasy.domain.member.dto.response.MemberResponseDto;
import com.efub.bageasy.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    // 회원가입 시 닉네임 설정
    @PutMapping("/oauth/nickname/{email}")
    public MemberResponseDto nicknameSet(@PathVariable String email, @RequestBody NicknameRequestDto requestDto){
        Member member = memberService.updateNickname(email, requestDto);
        return new MemberResponseDto(member);
    }


    // 프로필 조회

    // 파견교 설정
}
