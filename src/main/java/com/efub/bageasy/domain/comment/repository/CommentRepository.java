package com.efub.bageasy.domain.comment.repository;

import com.efub.bageasy.domain.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPostIdOrderByCreatedAt(Long postId);
}
