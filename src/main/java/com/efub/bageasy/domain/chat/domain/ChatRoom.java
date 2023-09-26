package com.efub.bageasy.domain.chat.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;

@Getter
@RedisHash(value = "chatRoom")
public class ChatRoom {
    @Id
    private String id;

    @Indexed
    private String nickname;

    @Indexed
    private Long roomId;

    @Indexed
    private String sessionId;

    @Builder
    public ChatRoom(Long roomId, String nickname, String sessionId){
        this.roomId = roomId;
        this.nickname = nickname;
        this.sessionId = sessionId;
    }
}
