package com.efub.bageasy.domain.chat.dto.response;

import com.efub.bageasy.domain.chat.domain.Chat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatResponseDto {
    private String id;
    private Long roomId;
    private Long senderId;
    private Long nickname;
    private String contentType;
    private String content;
    private long sentAt;
    private int readCount;
    private boolean isMine; //유저가 보낸 메세지인지

    public ChatResponseDto(Chat chat, Long memberId) {
        this.id = id;
        this.roomId = roomId;
        this.senderId = senderId;
        this.nickname = nickname;
        this.contentType = contentType;
        this.content = content;
        this.sentAt = sentAt;
        this.readCount = readCount;
        this.isMine = isMine;
    }
}
