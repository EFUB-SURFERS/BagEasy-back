package com.efub.bageasy.global.config.interceptor;

import com.efub.bageasy.domain.chat.dto.response.ChatRoomRecordDto;
import com.efub.bageasy.domain.chat.service.ChatRoomService;
import com.efub.bageasy.domain.chat.service.ChatService;
import com.efub.bageasy.domain.member.repository.MemberRepository;
import com.efub.bageasy.domain.member.service.JwtTokenProvider;
import com.efub.bageasy.domain.member.service.MemberService;
import com.efub.bageasy.global.exception.CustomException;
import com.efub.bageasy.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@RequiredArgsConstructor
@Component
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE + 99) //이벤트 우선 순위를 높게 설정
public class StompHandler implements ChannelInterceptor {
    private final JwtTokenProvider tokenProvider;
    private final ChatRoomService chatRoomService;
    private final ChatService chatService;
    private final MemberRepository memberRepository;

    // 메시지가 실제로 체널로 전송되기 전에 호출
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String nickname = verifyAccessToken(getAccessToken(accessor));
        log.info("StompAccessor = {}", accessor);
        handleMessage(accessor.getCommand(), accessor, nickname);
        return message;
    }

    private void handleMessage(StompCommand stompCommand, StompHeaderAccessor accessor, String nickname){
        switch (stompCommand){
            case CONNECT:
                log.info("websocket connected!!");
                connectToChatRoom(accessor, nickname);
                break;
            case SUBSCRIBE:
            case SEND:
                verifyAccessToken(getAccessToken(accessor));
                break;
            case DISCONNECT:
                chatService.updateReadOnDisconnect(getChatRoomId(accessor), nickname);
        }
    }

    private String getAccessToken(StompHeaderAccessor accessor){
        return accessor.getFirstNativeHeader("Authorization");
    }

    private String verifyAccessToken(String accessToken){
        if(!tokenProvider.validateToken(accessToken)){
            throw new CustomException(ErrorCode.EXPIRED_TOKEN);
        }

        return tokenProvider.getNicknameFromToken(accessToken);
    }

    private void connectToChatRoom(StompHeaderAccessor accessor, String nickname){
        Long roomId = getChatRoomId(accessor);
//
//        chatRoomService.connectToChatRoom(roomId, email);
//        chatService.updateCountAllZero(roomId, email);
        chatService.updateMessage(nickname, roomId);
    }

    private Long getChatRoomId(StompHeaderAccessor accessor){
        return Long.valueOf(Objects.requireNonNull(
                accessor.getFirstNativeHeader("roomId")));

    }
}
