package com.efub.bageasy.domain.heart.repository;

import com.efub.bageasy.domain.heart.domain.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface HeartRepository extends JpaRepository<Heart, Long>{
    Optional<Heart> findByMemberIdAndPostId(Long memberId, Long postId);
    Boolean existsHeartByMemberIdAndPostId(Long memberId, Long postId);
    List<Heart> findByMemberId(Long memberId);
    Long countByPostId(Long postId);
}
