package com.efub.bageasy.domain.member.controller;

import com.efub.bageasy.domain.member.domain.Member;
import com.efub.bageasy.domain.member.dto.request.LoginRequestDto;
import com.efub.bageasy.domain.member.dto.request.NicknameRequestDto;
import com.efub.bageasy.domain.member.dto.request.SchoolRequestDto;
import com.efub.bageasy.domain.member.dto.response.LoginResponseDto;
import com.efub.bageasy.domain.member.dto.response.MemberInfoDto;
import com.efub.bageasy.domain.member.dto.response.ReissueResponseDto;
import com.efub.bageasy.domain.member.service.AuthService;
import com.efub.bageasy.domain.member.service.MemberService;
import com.efub.bageasy.global.config.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping
public class MemberController {
    private final MemberService memberService;
    private final AuthService authService;

    @PostMapping("/auth/login")
    public LoginResponseDto login (@RequestBody LoginRequestDto requestDto) throws IOException{
        return authService.googleLogin(requestDto.getCode());
    }

    @PostMapping("/auth/reissue")
    public ReissueResponseDto tokenReissue(HttpServletRequest httpServletRequest){
        return authService.reissue(httpServletRequest);
    }

    @PutMapping("/members/nickname")
    public MemberInfoDto nicknameUpdate(@RequestBody NicknameRequestDto requestDto, @AuthUser Member member){
        return new MemberInfoDto(memberService.updateNickname(requestDto, member));   //트랜잭션
    }

    @PutMapping("/members/school")
    public MemberInfoDto schoolUpdate(@RequestBody SchoolRequestDto requestDto, @AuthUser Member member){
        return new MemberInfoDto(memberService.updateSchool(requestDto, member));
    }

    @GetMapping("/members/me")
    public MemberInfoDto profileGet(@AuthUser Member member){
        return new MemberInfoDto(member);
    }

    @GetMapping("/members/{nickname}")
    public MemberInfoDto memberFindByNickname(@PathVariable String nickname){
        return new MemberInfoDto(memberService.findMemberByNickname(nickname));
    }
}
