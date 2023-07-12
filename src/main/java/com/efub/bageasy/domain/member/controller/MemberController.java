package com.efub.bageasy.domain.member.controller;

import com.efub.bageasy.domain.member.domain.Member;
import com.efub.bageasy.domain.member.dto.*;
import com.efub.bageasy.domain.member.service.JwtTokenProvider;
import com.efub.bageasy.domain.member.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    /*
     * 유저 소셜 로그인으로 리다이렉트 해주는 url
     * [GET] /accounts/auth
     * @return void
     */
    @GetMapping("/auth/google")
    public void socialLoginRedirect() throws IOException {
        memberService.request();
    }

    /*
     * Social Login API Server 요청에 의한 callback 을 처리
     * @param socialLoginPath (GOOGLE, FACEBOOK, NAVER, KAKAO)
     * @param code API Server 로부터 넘어오는 code
     * @return SNS Login 요청 결과로 받은 Json 형태의 java 객체 (access_token, jwt_token, user_num 등)
     */


    @ResponseBody
    @GetMapping(value = "/auth/google/callback")
    public LoginResponseDto callback (@RequestParam(name = "code") String code)throws IOException{
        System.out.println(">> 소셜 로그인 API 서버로부터 받은 code :"+ code);

        return memberService.googleLogin(code);
    }

    @PostMapping("/social-login")
    public ResponseEntity<LoginResponse> doSocialLogin(@RequestBody @Valid SocialLoginRequest request) throws JsonProcessingException {

        return ResponseEntity.created(URI.create("/social-login"))
                .body(memberService.doSocialLogin(request));
    }

    @PutMapping("/profile/nickname")
    public MemberInfoDto nicknameCreate(@RequestBody NicknameRequestDto requestDto, HttpServletRequest httpServletRequest){
        MemberInfoDto memberInfoDto = jwtTokenProvider.getMemberInfoByRequest(httpServletRequest);
        //Member member = memberService.findMemberByAuth();
        Long memberId = memberInfoDto.getMemberId();
        System.out.println("memberId = "+memberId);
        Member member = memberService.findMemberById(memberId);
        return new MemberInfoDto(member.updateNickname(requestDto.getNickname()));
    }




}
