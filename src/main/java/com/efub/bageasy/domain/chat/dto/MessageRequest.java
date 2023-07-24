package com.efub.bageasy.domain.chat.dto;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MessageRequest {
    private Long roomId;
    private Long senderId;
    private String message;
}
