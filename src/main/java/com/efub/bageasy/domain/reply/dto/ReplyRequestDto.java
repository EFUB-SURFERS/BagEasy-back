package com.efub.bageasy.domain.reply.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReplyRequestDto {
    private String replyContent;
    private Boolean isSecret;
}
