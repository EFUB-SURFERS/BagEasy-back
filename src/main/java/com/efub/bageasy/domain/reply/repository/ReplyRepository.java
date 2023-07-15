package com.efub.bageasy.domain.reply.repository;

import com.efub.bageasy.domain.reply.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply,Long> {

    List<Reply> findAllByCommentId(Long commentId);
}
