package com.efub.bageasy.domain.member.controller;

import com.efub.bageasy.domain.member.domain.Member;
import com.efub.bageasy.domain.member.dto.request.LoginRequestDto;
import com.efub.bageasy.domain.member.dto.request.NicknameRequestDto;
import com.efub.bageasy.domain.member.dto.request.SchoolRequestDto;
import com.efub.bageasy.domain.member.dto.response.LoginResponseDto;
import com.efub.bageasy.domain.member.dto.response.MemberInfoDto;
import com.efub.bageasy.domain.member.service.MemberService;
import com.efub.bageasy.global.config.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/auth/login")
    public LoginResponseDto login (@RequestBody LoginRequestDto requestDto) throws IOException{
        return memberService.googleLogin(requestDto.getCode());
    }

    @PutMapping("/members/profile/nickname")
    public MemberInfoDto nicknameUpdate(@RequestBody NicknameRequestDto requestDto, @AuthUser Member member){
        return new MemberInfoDto(memberService.updateMember(member, requestDto));   //트랜잭션
    }

    @PutMapping("/members/school")
    public MemberInfoDto schoolUpdate(@RequestBody SchoolRequestDto requestDto, @AuthUser Member member){
        return new MemberInfoDto(memberService.updateSchool(requestDto, member));
    }
}
