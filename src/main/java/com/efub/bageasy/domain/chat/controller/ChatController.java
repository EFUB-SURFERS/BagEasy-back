package com.efub.bageasy.domain.chat.controller;

import com.efub.bageasy.domain.chat.dto.Message;
import com.efub.bageasy.domain.chat.dto.request.RoomCreateRequest;
import com.efub.bageasy.domain.chat.dto.response.*;
import com.efub.bageasy.domain.chat.service.ChatService;
import com.efub.bageasy.domain.chat.service.ChatRoomService;
import com.efub.bageasy.domain.member.domain.Member;
import com.efub.bageasy.global.config.AuthUser;
import com.efub.bageasy.global.exception.CustomException;
import com.efub.bageasy.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final ChatService chatService;
    private final ChatRoomService chatRoomService;

    /* 채팅방 생성 */
    @PostMapping("/chatrooms")
    public ResponseEntity<RoomCreateResponse> createChatRoom(@RequestBody @Valid final RoomCreateRequest roomCreateRequest, BindingResult bindingResult, @AuthUser Member member){
        if(bindingResult.hasErrors()){
            throw new CustomException(ErrorCode.ARGUMENT_NOT_VALID);
        }
        RoomCreateResponse response = chatService.makeChatRoom(member, roomCreateRequest);
        return ResponseEntity.ok(response);

    }


    /* 메세지 전송 */
    @MessageMapping("/message")
    public void sendMessage(@Valid Message message, @Header("Authorization") final String token) throws IOException {
        chatService.sendMessage(message,token);
    }

    /* 채팅 리스트 조회 */
    @GetMapping("/chatrooms/{roomId}")
    public ResponseEntity<ChatRoomRecordDto> getChatList(@PathVariable Long roomId,@AuthUser Member member){
        ChatRoomRecordDto chatList = chatService.getChatList(roomId, member);
        return ResponseEntity.ok(chatList);
    }

    /* 채팅방 리스트 조회 */
    @GetMapping("/chatrooms")
    public ResponseEntity<List<ChatRoomResponseDto>> getChatRoomList(@AuthUser Member member){
       List<ChatRoomResponseDto> chatRoomList = chatService.getChatRoomList(member);
        return ResponseEntity.ok(chatRoomList);
    }

    /* 채팅방 정보 조회 */
    @GetMapping("/chatrooms/info/{roomId}")
    public ResponseEntity<RoomInfoDto> getRoomInfoById(@PathVariable Long roomId){
        RoomInfoDto roomInfoDto = chatService.getRoomInfo(roomId);
        return ResponseEntity.ok(roomInfoDto);
    }

    /* 채팅 메세지 저장 */
    @PostMapping("/chatrooms/callback")
    public ResponseEntity<Message> saveMessage(@Valid @RequestBody Message message){
        Message savedMessaage = chatService.saveMessage(message);
        return ResponseEntity.ok(savedMessaage);
    }

//    @GetMapping("/mailtest")
//    public void sendEmailTest() throws MessagingException {
//        chatService.sendUnreadChatNotification();
//    }

}
