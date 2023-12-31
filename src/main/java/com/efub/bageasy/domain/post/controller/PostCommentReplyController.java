package com.efub.bageasy.domain.post.controller;

import com.efub.bageasy.domain.comment.domain.Comment;
import com.efub.bageasy.domain.comment.dto.CommentRequestDto;
import com.efub.bageasy.domain.comment.dto.CommentResponseDto;
import com.efub.bageasy.domain.comment.service.CommentService;
import com.efub.bageasy.domain.member.domain.Member;
import com.efub.bageasy.domain.member.service.MemberService;
import com.efub.bageasy.domain.notice.domain.Notice;
import com.efub.bageasy.domain.notice.service.NoticeService;
import com.efub.bageasy.domain.reply.domain.Reply;
import com.efub.bageasy.domain.reply.dto.ReplyRequestDto;
import com.efub.bageasy.domain.reply.dto.ReplyResponseDto;
import com.efub.bageasy.domain.reply.service.ReplyService;
import com.efub.bageasy.global.config.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class PostCommentReplyController {
    private final CommentService commentService;
    private final ReplyService replyService;
    private final MemberService memberService;
    private final NoticeService noticeService;

    // 양도글의 댓글 생성
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto createComment(@AuthUser Member member,
                                            @PathVariable Long postId, @RequestBody @Valid  CommentRequestDto requestDto){

        Comment comment = commentService.addComment(member,postId,requestDto);
        String writer = memberService.findNicknameById(comment.getMemberId());
        noticeService.createCommentNotice(member,comment);

        return new CommentResponseDto(comment ,writer);
    }

    //대댓글 생성
    @PostMapping("/{commentId}/replies")
    @ResponseStatus(HttpStatus.CREATED)
    public ReplyResponseDto createReply(@AuthUser Member member,
                                        @PathVariable Long postId, @PathVariable Long commentId,
                                        @RequestBody @Valid ReplyRequestDto requestDto)
    {
        Reply reply = replyService.addReply(member,commentId,requestDto);
        String writer = memberService.findNicknameById(reply.getMemberId());
        noticeService.createReplyNotice(member,reply);

        return new ReplyResponseDto(reply, writer);

    }

    //양도글의 모든 댓글 조회
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentResponseDto> getCommentList(@PathVariable Long postId){
        List<Comment> commentList = commentService.findCommentList(postId);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for(Comment comment:commentList){
            String writer = memberService.findNicknameById(comment.getMemberId());
            commentResponseDtoList.add(new CommentResponseDto(comment, writer));
        }

        return commentResponseDtoList;
    }




}
