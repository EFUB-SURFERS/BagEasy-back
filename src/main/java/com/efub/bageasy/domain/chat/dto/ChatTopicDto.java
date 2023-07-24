package com.efub.bageasy.domain.chat.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatTopicDto {
    @NotNull
    private String send; // 웹소켓 pub 주소
    @NotNull
    private Message message; // 보낼 내용
}
