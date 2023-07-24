package com.efub.bageasy.domain.chat.repository;

import com.efub.bageasy.domain.chat.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByRoomId(Long roomId);
}
