package com.efub.bageasy.domain.notice.service;

import com.efub.bageasy.domain.comment.domain.Comment;
import com.efub.bageasy.domain.comment.service.CommentService;
import com.efub.bageasy.domain.member.domain.Member;
import com.efub.bageasy.domain.member.service.MemberService;
import com.efub.bageasy.domain.notice.domain.Notice;
import com.efub.bageasy.domain.notice.dto.NoticeResponseDto;
import com.efub.bageasy.domain.notice.repository.NoticeRepository;
import com.efub.bageasy.domain.post.domain.Post;
import com.efub.bageasy.domain.post.repository.PostRepository;
import com.efub.bageasy.domain.post.service.PostService;
import com.efub.bageasy.domain.reply.domain.Reply;
import com.efub.bageasy.domain.reply.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {

    private final PostService postService;
    private final CommentService commentService;
    private final ReplyService replyService;
    private final MemberService memberService;
    private final NoticeRepository noticeRepository;

    /* 댓글 알림 생성 */
    public void createCommentNotice(Member member, Comment comment) {
        Post post = postService.findPost(comment.getPostId());
        Notice notice = new Notice(comment.getCommentId(),comment.getPostId(), post.getMemberId(),member.getMemberId(), post.getMemberId());
        noticeRepository.save(notice);
    }

    /* 대댓글 알림 생성 */
    public void createReplyNotice(Member member, Reply reply) {
        Comment comment = commentService.findComment(reply.getCommentId());
        Post post = postService.findPost(comment.getPostId());
        Notice notice = new Notice(reply.getReplyId(),comment.getPostId(),post.getMemberId(),comment.getCommentId(),
                member.getMemberId(), comment.getMemberId());
        noticeRepository.save(notice);
    }

    /* 회원 별 알림 목록 조회 */
    public List<NoticeResponseDto> findNoticeListByMember(Member member) {
        List<Notice> noticeList = findNoticeListByMember(member.getMemberId());
        List<NoticeResponseDto> responseDtoList = new ArrayList<>();

        for(Notice notice : noticeList){
            String postWriterNickName = memberService.findNicknameById(notice.getPostWriterId());
            String senderNickName = memberService.findNicknameById(notice.getSenderId());
            String targetNickName = memberService.findNicknameById(notice.getTargetId());
            String noticeContent;
            if(notice.getNoticeType().equals("comment")){
                noticeContent = commentService.findComment(notice.getContentId()).getContent();
            }
            else {
                noticeContent = replyService.findReply(notice.getContentId()).getContent();
            }
            responseDtoList.add(new NoticeResponseDto(notice,postWriterNickName,senderNickName,targetNickName,noticeContent));
        }

        return responseDtoList;
    }

    /* 회원 별 알림 목록 조회 : 알림 목록 반환 */
    public List<Notice> findNoticeListByMember(Long memberId){
        List<Notice> noticeList = noticeRepository.findAllByPostWriterIdOrTargetIdOrderByCreatedAtDesc(memberId , memberId);
        return noticeList;
    }
}
