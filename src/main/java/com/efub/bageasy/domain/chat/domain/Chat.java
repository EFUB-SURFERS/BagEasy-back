package com.efub.bageasy.domain.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class Chat {
    @Id
    private String id;
    private Long roomId;
    private Long senderId;
    private String nickname;
    private int type;  // text:0, image:1
    private String contentType; // notice 또는 talk
    private String content;
    private LocalDateTime sentAt;
}
