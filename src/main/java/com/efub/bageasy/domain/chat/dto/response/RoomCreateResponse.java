package com.efub.bageasy.domain.chat.dto.response;

import com.efub.bageasy.domain.chat.domain.Room;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomCreateResponse {
    private Long roomId;

    public RoomCreateResponse(Room room){
        this.roomId = room.getRoomId();
    }

}
