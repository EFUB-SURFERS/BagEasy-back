package com.efub.bageasy.domain.comment.controller;

import com.efub.bageasy.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor    // 생성자를 통한 의존관계 주입
@RequestMapping("/comments/{commentId}")
public class CommentController {
    private final CommentService commentService;
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public String deleteComment(@PathVariable Long commentId){
        commentService.deleteComment(commentId);
        return "댓글이 삭제되었습니다.";

    }
}
