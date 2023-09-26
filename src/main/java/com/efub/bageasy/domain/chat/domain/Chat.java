package com.efub.bageasy.domain.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Chat {
    @Id
    private String id;
    private Long roomId;
    private String nickname;
    private int type;  // text:0, image:1
    private String contentType; // notice 또는 talk
    private String content;
    private LocalDateTime sentAt;
    private Integer readCount;
}
