package com.efub.bageasy.domain.notice.domain;

import com.efub.bageasy.global.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Getter
public class Notice extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long noticeId;

    /* 알림 유형 : 댓글 , 대댓글 */
    @Column(name = "notice_type" , nullable = false)
    private String noticeType;

    /* 알림 내용 id : 작성된 댓글/대댓글의 id */
    @Column(name = "content_id" , nullable = false)
    private Long contentId;

    /* 게시글 id : 댓글/대댓글이 작성된 게시글 */
    @Column(name = "post_id" , nullable = false)
    private Long postId;

    /* 게시글 작성자 id */
    @Column(name = "post_writer_id" , nullable = false)
    private Long postWriterId;

    /* 댓글 id  : 대댓글이 작성된 게시글 */
    @Column(name = "comment_id")
    private Long commentId;

    /* 댓글/대댓글 작성자 id */
    @Column(name = "sender_id" , nullable = false)
    private Long senderId;

    /* 게시글/원댓글 작성자 id */
    @Column(name = "target_id" , nullable = false)
    private Long targetId;

    /* 알림 확인 여부 , default 값은 false */
    @Column(name = "is_checked")
    private Boolean isChecked;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /* 댓글 알림 */
    @Builder
    public Notice(Long contentId, Long postId, Long postWriterId,Long senderId, Long targetId , LocalDateTime createdAt){
        this.noticeType="comment";
        this.contentId = contentId;
        this.postId = postId;
        this.postWriterId =postWriterId;
        this.senderId = senderId;
        this.targetId= targetId;
        this.isChecked= Boolean.valueOf("false");
        this.createdAt = createdAt;
    }

    /* 대댓글 알림 */
    @Builder
    public Notice(Long contentId, Long postId, Long postWriterId, Long commentId ,Long senderId, Long targetId ,LocalDateTime createdAt){
        this.noticeType="reply";
        this.contentId = contentId;
        this.postId = postId;
        this.postWriterId = postWriterId;
        this.commentId = commentId;
        this.senderId = senderId;
        this.targetId= targetId;
        this.isChecked= Boolean.valueOf("false");
        this.createdAt = createdAt;
    }

    // 알림 확인
    public void updateIsChecked(){
        this.isChecked = Boolean.valueOf("true");
    }

}
