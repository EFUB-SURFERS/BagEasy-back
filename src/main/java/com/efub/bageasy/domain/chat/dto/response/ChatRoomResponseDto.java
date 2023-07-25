package com.efub.bageasy.domain.chat.dto.response;

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
    private Long createMember;
    private Long joinMember;
    private Long postId;

    private LatestMessage latestMessage;

    private Participant participant;
//    private Integer unReadCount;

//    public void setUnReadCount(Integer unReadCount){
//        this.unReadCount = unReadCount;
//    }

    public void setLatestMessage(LatestMessage latestMessage){
        this.latestMessage = latestMessage;
    }

    public ChatRoomResponseDto(Long roomId, Long createMember, Long joinMember, Long postId, Participant participant ) {
        this.roomId = roomId;
        this.createMember = createMember;
        this.joinMember = joinMember;
        this.postId = postId;
        this.participant = participant;

    }

    @Getter
    @AllArgsConstructor
    @ToString
    public static class Participant{
        private String nickname;
    }

    @Getter
    @ToString
    public static class LatestMessage{
        private String context;
        private long sentAt;
        private String content;

        @Builder
        public LatestMessage(String content, LocalDateTime sentAt) {
            this.content = content;
            this.sentAt = sentAt.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        }
    }
}
