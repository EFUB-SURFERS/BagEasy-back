package com.efub.bageasy.domain.chat.dto.response;

import com.efub.bageasy.domain.chat.domain.Room;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomInfoDto {
    private Long roomId;
    private String joinMember;
    private String createMember;
    private Long postId;

    @Builder
    public RoomInfoDto(Room room, String joinMember, String createMember) {
        this.roomId = room.getRoomId();
        this.joinMember = joinMember;
        this.createMember = createMember;
        this.postId = room.getPostId();
    }
}
