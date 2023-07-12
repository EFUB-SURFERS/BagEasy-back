package com.efub.bageasy.domain.member.controller;

import com.efub.bageasy.domain.member.domain.Member;
import com.efub.bageasy.domain.member.dto.*;
import com.efub.bageasy.domain.member.service.JwtTokenProvider;
import com.efub.bageasy.domain.member.service.MemberService;
import com.efub.bageasy.global.config.AuthUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    /*
     * 유저 소셜 로그인으로 리다이렉트 해주는 url
     */
    @GetMapping("/auth/google")
    public void socialLoginRedirect() throws IOException {
        memberService.request();
    }


    @ResponseBody
    @GetMapping(value = "/auth/google/callback")
    public LoginResponseDto callback (@RequestParam(name = "code") String code)throws IOException{
        return memberService.googleLogin(code);
    }

    @PostMapping("/social-login")
    public ResponseEntity<LoginResponse> doSocialLogin(@RequestBody @Valid SocialLoginRequest request) throws JsonProcessingException {
        return ResponseEntity.created(URI.create("/social-login"))
                .body(memberService.doSocialLogin(request));
    }

    @PutMapping("/profile/nickname")
    public MemberInfoDto nicknameCreate(@RequestBody NicknameRequestDto requestDto, @AuthUser Member member){
        member.updateNickname(requestDto.getNickname());
        return new MemberInfoDto(member);
    }





}
