package com.efub.bageasy.domain.chat.mongo;

import com.efub.bageasy.domain.chat.domain.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MongoChatRepository extends MongoRepository<Chat, String> {
    List<Chat> findByRoomId(Long roomId);

    Page<Chat> findByRoomIdAndSentAt(Long roomId, Pageable pageable);

    Page<Chat> findByRoomIdOrderBySentAtDesc(Long roomId, Pageable pageable);
}
