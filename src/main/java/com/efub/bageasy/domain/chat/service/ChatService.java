package com.efub.bageasy.domain.chat.service;

import com.efub.bageasy.domain.chat.domain.Chat;
import com.efub.bageasy.domain.chat.domain.Room;
import com.efub.bageasy.domain.chat.dto.Message;
import com.efub.bageasy.domain.chat.dto.request.RoomCreateRequest;
import com.efub.bageasy.domain.chat.dto.response.*;
import com.efub.bageasy.domain.chat.mongo.MongoChatRepository;
import com.efub.bageasy.domain.chat.repository.ChatQuerydslRepository;
import com.efub.bageasy.domain.chat.repository.RoomRepository;
import com.efub.bageasy.domain.member.domain.Member;
import com.efub.bageasy.domain.member.repository.MemberRepository;
import com.efub.bageasy.domain.member.service.JwtTokenProvider;
import com.efub.bageasy.domain.post.domain.Post;
import com.efub.bageasy.domain.post.repository.PostRepository;
import com.efub.bageasy.global.config.kafka.KafkaConstants;
import com.efub.bageasy.global.exception.CustomException;
import com.efub.bageasy.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final PostRepository postRepository;
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final MongoChatRepository mongoChatRepository;
    private final ChatQuerydslRepository chatQuerydslRepository;
    private final KafkaProducer kafkaProducer;
    private final KafkaConstants kafkaConstants;
    private final JwtTokenProvider tokenProvider;

    public RoomCreateResponse makeChatRoom(Long memberId, RoomCreateRequest roomCreateRequest) {
        Post post = postRepository.findPostByPostId(roomCreateRequest.getPostId()).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_EXIST));

        //판매완료된 게시글
        if(post.getIsSold()){
            throw new CustomException(ErrorCode.POST_SOLDOUT);
        }


        //존재하지 않는 멤버 아이디
        Member createMember = memberRepository.findMemberByNickname(roomCreateRequest.getCreateMember())
                .orElseThrow(() -> new CustomException(ErrorCode.NO_MEMBER_EXIST));

        //자기자신을 채팅방에 초대하는 경우
        if(memberId.equals(post.getMemberId())){
            throw new CustomException(ErrorCode.ROOM_MEMBER_DUPLICATE);
        }

        Room room = Room.builder()
                .postId(roomCreateRequest.getPostId())
                .sellerId(post.getMemberId())
                .buyerId(createMember.getMemberId())
                .build();

        Room savedRoom = roomRepository.save(room);
//        kafkaProducer.sendChat("kafka_chat", );
        return new RoomCreateResponse(savedRoom);
    }

    public void sendMessage(Message message, String token){
//        boolean isConnectAll = chatRoomService.isAllConnected(message.getRoomId());
//        Integer readCount = isConnectAll ? 0 : 1;
        String nickname = tokenProvider.getNicknameFromToken(token);
        message.setSendTimeAndSender(LocalDateTime.now(), nickname);
        kafkaProducer.sendChat(kafkaConstants.getTopic(), message);
    }

    public ChatRoomRecordDto getChatList(Long roomId, Member member){
//        updateCountZero(roomId, member);
        Room room = roomRepository.findByRoomId(roomId).orElseThrow(()-> new CustomException(ErrorCode.ROOM_NOT_EXIST));
        Member createMember = memberRepository.findByMemberId(room.getBuyerId())
                .orElseThrow(()-> new CustomException(ErrorCode.NO_MEMBER_EXIST));
        Member joinMember = memberRepository.findByMemberId(room.getSellerId())
                .orElseThrow(()-> new CustomException(ErrorCode.NO_MEMBER_EXIST));
        String createMemNickname = createMember.getNickname();
        String joinMemNickname = joinMember.getNickname();

        List<ChatResponseDto> chatList = mongoChatRepository.findByRoomId(roomId)
                .stream()
                .map(e -> new ChatResponseDto(e,member.getNickname()))
                .collect(Collectors.toList());

        return ChatRoomRecordDto.builder()
                .chatList(chatList)
                .nickname(member.getNickname().equals(createMemNickname)?joinMemNickname:createMemNickname)
                .build();
    }

    public List<ChatRoomResponseDto> getChatRoomList(Member member) {
        List<Room> chatRooms = chatQuerydslRepository.getChatRooms(member.getMemberId());

        List<ChatRoomResponseDto> chatRoomList = new ArrayList<>();

        for(Room room : chatRooms){
            Page<Chat> chatting = mongoChatRepository.findByRoomIdOrderBySentAtDesc(room.getRoomId(), PageRequest.of(0, 1));
            if (chatting.hasContent()) {
                Chat chat = chatting.getContent().get(0);
                ChatRoomResponseDto.LatestMessage latestMessage = ChatRoomResponseDto.LatestMessage.builder()
                        .content(chat.getContent())
                        .sentAt(chat.getSentAt())
                        .build();
                String createMember = memberRepository.findByMemberId(room.getBuyerId())
                        .orElseThrow(() -> new CustomException(ErrorCode.NO_MEMBER_EXIST)).getNickname();
                String joinMember = memberRepository.findByMemberId(room.getSellerId())
                        .orElseThrow(() -> new CustomException(ErrorCode.NO_MEMBER_EXIST)).getNickname();


                ChatRoomResponseDto chatRoomResponseDto = ChatRoomResponseDto.builder()
                        .room(room)
                        .createMember(createMember)
                        .joinMember(joinMember)
                        .latestMessage(latestMessage)
                        .build();

                chatRoomList.add(chatRoomResponseDto);
            }
        }

        log.info(String.valueOf(chatRoomList.size()));

        return chatRoomList;
    }


    // 유저가 입장하면 readCount를 0으로 업데이트
//    public void updateCountZero(Long roomId, Member member){
//        Update update = new Update().set("readCount",0);
//        Query query = new Query(Criteria.where("roomId").is("roomId").and("senderId").ne(member.getMemberId()));
//        mongoTemplate.updateMulti(query, update, Chat.class);
//    }

    public void updateMessage(String nickname,Long roomId){
        Message message = Message.builder()
                .contentType("notice")
                .roomId(roomId)
                .content(nickname + "님이 돌아오셨습니다.")
                .build();

        kafkaProducer.sendChat(kafkaConstants.getTopic(), message);

    }


    @Transactional(readOnly = true)
    public RoomInfoDto getRoomInfo(Long roomId) {
        Room room = roomRepository.findByRoomId(roomId).orElseThrow(()-> new CustomException(ErrorCode.ROOM_NOT_EXIST));
        String createMember = memberRepository.findByMemberId(room.getBuyerId())
                .orElseThrow(()-> new CustomException(ErrorCode.NO_MEMBER_EXIST))
                .getNickname();
        String joinMember = memberRepository.findByMemberId(room.getSellerId())
                .orElseThrow(()-> new CustomException(ErrorCode.NO_MEMBER_EXIST))
                .getNickname();

        RoomInfoDto roomInfoDto =  RoomInfoDto.builder()
                .room(room)
                .joinMember(joinMember)
                .createMember(createMember)
                .build();

        return roomInfoDto;

    }

    // 실제 메시지 발신인과 callback으로 메시지 저장을 요청한 사람이 일치해야만 db에 저장하여 중복 방지
    public Message saveMessage(Message message) {
        if(!memberRepository.existsMemberByNickname(message.getNickname())){
            throw new CustomException(ErrorCode.NO_MEMBER_EXIST);
        }
        if(message.getNickname().equals(message.getCallbackNickname())){
            Chat chat = message.convertEntity();
            log.info(String.valueOf(chat.getSentAt()));
            Chat savedChat = mongoChatRepository.save(chat);
            message.setId(savedChat.getId());
        }
        return message;
    }
}
