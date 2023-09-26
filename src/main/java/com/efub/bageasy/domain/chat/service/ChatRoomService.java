package com.efub.bageasy.domain.chat.service;

import com.efub.bageasy.domain.chat.domain.ChatRoom;
import com.efub.bageasy.domain.chat.repository.ChatRoomRepository;
import com.efub.bageasy.global.exception.CustomException;
import com.efub.bageasy.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public void setChatRoomEnterInfo(Long roomId, String nickname, String sessionId){
        ChatRoom chatRoom = ChatRoom.builder()
                .nickname(nickname)
                .roomId(roomId)
                .sessionId(sessionId)
                .build();

        chatRoomRepository.save(chatRoom);
    }

    public void deleteChatRoomEnterInfo(String sessionId){
        ChatRoom chatRoom = chatRoomRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new CustomException(ErrorCode.ARGUMENT_NOT_VALID));
        chatRoomRepository.delete(chatRoom);
    }

    public int countMemberEntered(Long roomId){
        List<ChatRoom> enteredInfoList  = chatRoomRepository.findByRoomId(roomId);
        return enteredInfoList.size();
    }


}
