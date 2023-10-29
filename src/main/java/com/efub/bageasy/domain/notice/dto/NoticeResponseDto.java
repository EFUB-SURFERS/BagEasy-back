package com.efub.bageasy.domain.notice.dto;


import com.efub.bageasy.domain.notice.domain.Notice;
import com.efub.bageasy.domain.post.dto.PostResponseDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class NoticeResponseDto {
    private Long noticeId;
    private String noticeType;
    private String noticeContent;
    private Long postId;
    private Long commentId;
    private String postWriterNickname;
    private String senderNickname;
    private String targetNickname;
    private Boolean isChecked;

    public NoticeResponseDto(Notice notice , String postWriterNickName,String senderNickname
            , String targetNickname , String noticeContent ){
        this.noticeId = notice.getNoticeId();
        this.noticeType = notice.getNoticeType();
        this.noticeContent = noticeContent;
        this.postId = notice.getPostId();
        this.commentId = notice.getCommentId();
        this.postWriterNickname = postWriterNickName;
        this.senderNickname = senderNickname;
        this.targetNickname = targetNickname;
        this.isChecked = notice.getIsChecked();

    }
}
