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

//    private Participant participant;
//    private Integer unReadCount;

//    public void setUnReadCount(Integer unReadCount){
//        this.unReadCount = unReadCount;
//    }

    public void setLatestMessage(LatestMessage latestMessage){
        this.latestMessage = latestMessage;
    }

    @Builder
    public ChatRoomResponseDto(Room room, String createMember, String joinMember, LatestMessage latestMessage ) {
        this.roomId = room.getRoomId();
        this.createMember = createMember;
        this.joinMember = joinMember;
        this.postId = room.getPostId();
        this.latestMessage = latestMessage;

    }

//    @Getter
//    @AllArgsConstructor
//    @ToString
//    public static class Participant{
//        private String nickname;
//    }

    @Getter
    @ToString
    public static class LatestMessage{
        private long sentAt;
        private String content;

        @Builder
        public LatestMessage(String content, LocalDateTime sentAt) {
            this.content = content;
            this.sentAt = sentAt.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        }
    }
}
