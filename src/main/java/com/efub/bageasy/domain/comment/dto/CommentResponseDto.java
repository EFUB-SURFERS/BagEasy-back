package com.efub.bageasy.domain.comment.dto;

import com.efub.bageasy.domain.comment.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
@AllArgsConstructor
@Builder
public class CommentResponseDto {
    private Long commentId;
    private String writer;
    private Long postId;
    private String commentContent;
    private Boolean isSecret;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public CommentResponseDto(Comment comment, String writer){
        this.commentId=comment.getCommentId();
        this.writer = writer;
        this.postId=comment.getPostId();
        this.commentContent=comment.getContent();
        this.isSecret=comment.getIsSecret();
        this.createdAt=comment.getCreatedAt();
        this.modifiedAt=comment.getModifiedAt();

    }
}
