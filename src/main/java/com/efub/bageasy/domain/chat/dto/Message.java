package com.efub.bageasy.domain.chat.dto;

import com.efub.bageasy.domain.chat.domain.Chat;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {
    private String id;


    @NotNull
    private Long roomId;

    @NotNull
    private String contentType;

    @NotNull
    private String content;


    private String nickname;

    private Long sentAt;

    private String callbackNickname;


    @NotNull
    private int type;

    private Integer readCount;
    public void setSendTimeAndSender(LocalDateTime sentAt, String nickname, Integer readCount){
        this.nickname = nickname;
        this.sentAt = sentAt.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        this.readCount = readCount;
    }

    public void setImageUrl(String imgUrl){
        this.content = imgUrl;
    }

    public void setId(String id){
        this.id = id;
    }

    public Chat convertEntity(){
        return Chat.builder()
                .nickname(nickname)
                .roomId(roomId)
                .type(type)
                .contentType(contentType)
                .content(content)
                .sentAt(Instant.ofEpochMilli(sentAt).atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime())
                .readCount(readCount)
                .build();
    }


}
