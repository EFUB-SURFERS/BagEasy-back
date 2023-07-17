package com.efub.bageasy.domain.comment.service;

import com.efub.bageasy.domain.comment.domain.Comment;
import com.efub.bageasy.domain.comment.dto.CommentRequestDto;
import com.efub.bageasy.domain.comment.repository.CommentRepository;
import com.efub.bageasy.domain.member.domain.Member;
import com.efub.bageasy.domain.post.domain.Post;
import com.efub.bageasy.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final PostService postService;
    private final CommentRepository commentRepository;

    // 댓글 생성
    public Comment addComment(Member member, Long postId, CommentRequestDto requestDto) {
        Post post = postService.findPost(postId);
        Comment comment = new Comment(member.getMemberId(),requestDto.getIsSecret(),requestDto.getCommentContent(),post.getPostId());
        commentRepository.save(comment);
        return comment;
    }

    // commentId로 댓글 찾기
    public Comment findComment(Long commentId){
        return commentRepository.findById(commentId)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 댓글입니다."));
    }

    public List<Comment> findCommentList(Long postId) {
        return commentRepository.findAllByPostIdOrderByCreatedAt(postId);
    }

    public void deleteComment(Long commentId, Member member) {
        Comment comment = findComment(commentId);
        if(comment.getMemberId() == member.getMemberId()) {
            commentRepository.delete(comment);
        }
    }
}
