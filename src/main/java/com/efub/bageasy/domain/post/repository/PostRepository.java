package com.efub.bageasy.domain.post.repository;

import com.efub.bageasy.domain.post.domain.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long>{
    List<Post> findAllByMemberIdOrderByModifiedAtDesc(Long memberId);
    List<Post> findAll(Sort sort);

    Optional<Post> findPostByPostId(Long postId);
  
    List<Post> findAllByBuyerId(Long buyerId);

    List<Post> findAllBySchoolOrderByModifiedAtDesc(String school);

}
