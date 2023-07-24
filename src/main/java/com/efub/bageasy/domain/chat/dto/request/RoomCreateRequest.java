package com.efub.bageasy.domain.chat.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class RoomCreateRequest {
    @NotNull
    private Long postId;
    @NotNull
    private String createMember;
}
