package com.efub.bageasy.domain.notice.repository;

import com.efub.bageasy.domain.notice.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice,Long> {
    List<Notice> findAllByPostWriterIdOrTargetIdOrderByCreatedAtDesc(Long postWriterId, Long targetId);
}
