package com.efub.bageasy.domain.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class ChatRoomRecordDto {
    private String nickname;
    private List<ChatResponseDto> chatList;

}
