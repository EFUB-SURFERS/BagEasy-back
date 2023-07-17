package com.efub.bageasy.domain.reply.Controller;

import com.efub.bageasy.domain.member.domain.Member;
import com.efub.bageasy.domain.reply.service.ReplyService;
import com.efub.bageasy.global.config.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/replies/{replyId}")
public class ReplyController {

    private final ReplyService replyService;
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public String deleteReply(@AuthUser Member member, @PathVariable Long replyId){

        replyService.deleteReply(member,replyId);
        return "대댓글이 삭제되었습니다";

    }
}
