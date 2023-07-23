package com.efub.bageasy.domain.post.repository;

import com.efub.bageasy.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long>{
    List<Post> findAllByMemberId(Long memberId);
    List<Post> findAllByBuyerId(Long buyerId);
}
