package com.efub.bageasy.domain.chat.dto.response;

import com.efub.bageasy.domain.chat.domain.Chat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZoneId;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatResponseDto {
    private String id;
    private Long roomId;
    private Long senderId;
    private String nickname;

    private int type;
    private String contentType;
    private String content;
    private long sentAt;
    private boolean isMine; //유저가 보낸 메세지인지

    public ChatResponseDto(Chat chat, Long memberId) {
        this.id = chat.getId();
        this.roomId = chat.getRoomId();
        this.senderId = chat.getSenderId();
        this.nickname = chat.getNickname();
        this.contentType = chat.getContentType();
        this.type = chat.getType();
        this.content = chat.getContent();
        this.sentAt = chat.getSentAt().atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        this.isMine = memberId.equals(senderId);
    }
}
