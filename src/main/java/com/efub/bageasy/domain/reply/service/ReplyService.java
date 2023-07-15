package com.efub.bageasy.domain.reply.service;

import com.efub.bageasy.domain.comment.domain.Comment;
import com.efub.bageasy.domain.comment.service.CommentService;
import com.efub.bageasy.domain.member.domain.Member;
import com.efub.bageasy.domain.post.service.PostService;
import com.efub.bageasy.domain.reply.domain.Reply;
import com.efub.bageasy.domain.reply.dto.ReplyRequestDto;
import com.efub.bageasy.domain.reply.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyService {
    private final PostService postService;
    private final CommentService commentService;

    private final ReplyRepository replyRepository;


    public Reply addReply(Member member, Long commentId, ReplyRequestDto requestDto) {
        Comment comment=commentService.findComment(commentId);

        Reply reply = new Reply(comment.getCommentId(), member.getMemberId(),
                requestDto.getReplyContent(),requestDto.getIsSecret());

        replyRepository.save(reply);
        return reply;

    }

    public Reply findReply(Long replyId){
        return replyRepository.findById(replyId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 게시글입니다."));
    }

    // 대댓글 목록 조회
    public List<Reply> findReplyList(Long commentId) {
        Comment comment = commentService.findComment(commentId);
        return replyRepository.findAllByCommentId(commentId);
    }

    //대댓글 삭제
    public void deleteReply(Member member, Long replyId) {
        Reply reply = findReply(replyId);
        replyRepository.delete(reply);
    }
}
