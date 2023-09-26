package com.efub.bageasy.domain.chat.dto.response;

import com.efub.bageasy.domain.chat.domain.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@ToString
public class ChatRoomResponseDto {
    private Long roomId;
    private String createMember;
    private String joinMember;
    private Long postId;
    private LatestMessage latestMessage;
    private Long unReadCount;

    @Builder
    public ChatRoomResponseDto(Room room, String createMember, String joinMember, LatestMessage latestMessage, Long unReadCount ) {
        this.roomId = room.getRoomId();
        this.createMember = createMember;
        this.joinMember = joinMember;
        this.postId = room.getPostId();
        this.latestMessage = latestMessage;
        this.unReadCount = unReadCount;
    }

    @Getter
    @ToString
    public static class LatestMessage{
        private long sentAt;
        private String content;
        private Boolean isMine;

        @Builder
        public LatestMessage(String content, LocalDateTime sentAt,Boolean isMine) {
            this.content = content;
            this.sentAt = sentAt.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
            this.isMine = isMine;
        }
    }
}
