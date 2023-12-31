package com.efub.bageasy.domain.reply.dto;

import com.efub.bageasy.domain.reply.domain.Reply;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReplyResponseDto {
    private Long replyId;
    private Long commentId;
    private String writer;
    private String replyContent;
    private Boolean isSecret;

    public ReplyResponseDto(Reply reply , String writer){
        this.replyId=reply.getReplyId();
        this.commentId=reply.getCommentId();
        this.writer = writer;
        this.replyContent=reply.getContent();
        this.isSecret=reply.getIsSecret();

    }


}
