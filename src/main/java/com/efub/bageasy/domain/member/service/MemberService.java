package com.efub.bageasy.domain.member.service;

import com.efub.bageasy.domain.member.domain.Member;
import com.efub.bageasy.domain.member.dto.request.NicknameRequestDto;
import com.efub.bageasy.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    public Member updateNickname(String email, NicknameRequestDto requestDto){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()->new EntityNotFoundException("해당 계정을 찾을 수 없습니다."));
        return member.updateNickname(requestDto.getNickname());
    }
}
