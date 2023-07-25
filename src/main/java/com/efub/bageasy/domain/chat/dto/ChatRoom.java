package com.efub.bageasy.domain.chat.dto;

import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;


import javax.persistence.Id;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
//@RedisHash(value = "chatRoom")
public class ChatRoom {
    @Id
    private String id;

    @Indexed
    private Long roomId;

    @Indexed
    private String email;

    @Builder
    public ChatRoom(Long roomId, String email){
        this.roomId = roomId;
        this.email = email;
    }

}
