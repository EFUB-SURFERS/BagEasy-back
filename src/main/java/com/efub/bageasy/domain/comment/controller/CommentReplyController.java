package com.efub.bageasy.domain.comment.controller;


import com.efub.bageasy.domain.member.service.MemberService;
import com.efub.bageasy.domain.reply.domain.Reply;
import com.efub.bageasy.domain.reply.dto.ReplyResponseDto;
import com.efub.bageasy.domain.reply.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor    // 생성자를 통한 의존관계 주입
@RequestMapping("/comments/{commentId}/replies")
public class CommentReplyController {
    private final ReplyService replyService;
    private final MemberService memberService;


    // 댓글의 모든 대댓글 조회
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ReplyResponseDto> getReplyList(@PathVariable Long commentId){
        List<Reply> replyList = replyService.findReplyList(commentId);
        List<ReplyResponseDto> responseDtoList = new ArrayList<>();

        for(Reply reply:replyList){
            String writer = memberService.findNicknameById(reply.getMemberId());

            responseDtoList.add(new ReplyResponseDto(reply , writer));
        }

        return responseDtoList;

    }
}
