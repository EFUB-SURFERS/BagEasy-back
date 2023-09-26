package com.efub.bageasy.domain.chat.repository;

import com.efub.bageasy.domain.chat.domain.ChatRoom;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends CrudRepository<ChatRoom, String> {
    List<ChatRoom> findByRoomId(Long roomId);

    Optional<ChatRoom> findByRoomIdAndEmail(Long roomId, String nickname);

    Optional<ChatRoom> findBySessionId(String sessionId);
}
