package com.efub.bageasy.domain.member.service;

import com.efub.bageasy.domain.member.domain.Member;
import com.efub.bageasy.domain.member.dto.request.NicknameRequestDto;
import com.efub.bageasy.domain.member.dto.request.SchoolRequestDto;
import com.efub.bageasy.domain.member.oauth.GoogleUser;
import com.efub.bageasy.domain.member.repository.MemberRepository;
import com.efub.bageasy.global.exception.CustomException;
import com.efub.bageasy.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    public Member saveMember(@RequestBody GoogleUser googleUser) {
        Member member = Member.builder()
                .email(googleUser.getEmail())
                .nickname(googleUser.getName())
                .build();
        memberRepository.save(member);
        return member;
    }

    public Boolean checkJoined(String email) {
        System.out.println("checkJoined email: " + email);
        Boolean isJoined = memberRepository.existsMemberByEmail(email);
        return isJoined;
    }

    @Transactional(readOnly = true)
    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_MEMBER_EXIST));
    }

    @Transactional(readOnly = true)
    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_MEMBER_EXIST));
    }

    @Transactional(readOnly = true)
    public Member findMemberByNickname(String nickname) {
        return memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_MEMBER_EXIST));
    }

    public String findNicknameById(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_MEMBER_EXIST));
        return member.getNickname();
    }


    public Member updateNickname(NicknameRequestDto requestDto, Member member) {
        String nickname = requestDto.getNickname();
        if (memberRepository.existsMemberByNickname(nickname))
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        else
            return member.updateNickname(requestDto.getNickname());
    }

    public Member updateSchool(SchoolRequestDto requestDto, Member member) {
        return member.updateSchool(requestDto.getSchool());
    }
}
