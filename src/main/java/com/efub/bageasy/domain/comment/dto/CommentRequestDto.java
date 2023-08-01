package com.efub.bageasy.domain.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequestDto {
    private String commentContent;
    private Boolean isSecret;

}
