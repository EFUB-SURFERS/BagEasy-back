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
    private String nickname;

    private int type;
    private String contentType;
    private String content;
    private long sentAt;
    private boolean isMine; //유저가 보낸 메세지인지
    private Integer readCount;

    public ChatResponseDto(Chat chat, String myNickname) {
        this.id = chat.getId();
        this.roomId = chat.getRoomId();
        this.nickname = chat.getNickname();
        this.contentType = chat.getContentType();
        this.type = chat.getType();
        this.content = chat.getContent();
        this.sentAt = chat.getSentAt().atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        this.readCount = chat.getReadCount();
        this.isMine = chat.getNickname().equals(myNickname);
    }
}
