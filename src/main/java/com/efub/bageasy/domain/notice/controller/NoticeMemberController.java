package com.efub.bageasy.domain.notice.controller;

import com.efub.bageasy.domain.member.domain.Member;
import com.efub.bageasy.domain.notice.dto.NoticeResponseDto;
import com.efub.bageasy.domain.notice.service.NoticeService;
import com.efub.bageasy.global.config.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/members/notices")
@RequiredArgsConstructor
public class NoticeMemberController {
    private final NoticeService noticeService;

    /* 회원별 알림 목록 조회 */
    @GetMapping
    public List<NoticeResponseDto> getNoticeList(@AuthUser Member member){
        return noticeService.findNoticeListByMember(member);
    }
}
