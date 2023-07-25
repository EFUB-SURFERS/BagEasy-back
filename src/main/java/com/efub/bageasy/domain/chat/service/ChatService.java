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
import com.efub.bageasy.domain.post.domain.Post;
import com.efub.bageasy.domain.post.repository.PostRepository;
import com.efub.bageasy.global.config.kafka.KafkaConstants;
import com.efub.bageasy.global.exception.CustomException;
import com.efub.bageasy.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {
    private final PostRepository postRepository;
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final MongoChatRepository mongoChatRepository;
    private final ChatQuerydslRepository chatQuerydslRepository;
    private final ChatRoomService chatRoomService;
    private final KafkaProducer kafkaProducer;
    private final MongoTemplate mongoTemplate;
    private final KafkaConstants kafkaConstants;

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
        if(memberId.equals(createMember.getMemberId())){
            throw new CustomException(ErrorCode.ROOM_MEMBER_DUPLICATE);
        }

        Room room = Room.builder()
                .postId(roomCreateRequest.getPostId())
                .sellerId(post.getMemberId())
                .buyerId(createMember.getMemberId())
                .build();

        Room savedRoom = roomRepository.save(room);
//        kafkaProducer.sendChat("kafka_chat", );
        return new RoomCreateResponse(room);
    }

    public void sendMessage(Message message, Member member){
//        boolean isConnectAll = chatRoomService.isAllConnected(message.getRoomId());
//        Integer readCount = isConnectAll ? 0 : 1;
        message.setSendTimeAndSender(LocalDateTime.now(), member.getMemberId(), member.getNickname());
        kafkaProducer.sendChat(kafkaConstants.getTopic(), message);
    }

    public ChatRoomRecordDto getChatList(Long roomId, Member member){
        updateCountZero(roomId, member);
        Room room = roomRepository.findByRoomId(roomId).orElseThrow(()-> new CustomException(ErrorCode.ROOM_NOT_EXIST));
        Member createMember = memberRepository.findByMemberId(room.getBuyerId()).orElseThrow(()-> new CustomException(ErrorCode.NO_MEMBER_EXIST));
        String createMemberNickname = createMember.getNickname();

        List<ChatResponseDto> chatList = mongoChatRepository.findByRoomId(roomId)
                .stream()
                .map(e -> new ChatResponseDto(e,member.getMemberId()))
                .collect(Collectors.toList());

        return ChatRoomRecordDto.builder()
                .chatList(chatList)
                .nickname(createMemberNickname)
                .build();
    }

    public List<ChatRoomResponseDto> getChatRoomList(Member member) {
        List<ChatRoomResponseDto> chatRoomList = chatQuerydslRepository.getChatRooms(member.getMemberId());
        chatRoomList.forEach( dto -> {
            Page<Chat> chatting = mongoChatRepository.findByRoomIdOrderBySentAtDesc(dto.getRoomId(), PageRequest.of(0,1));
            if(chatting.hasContent()){
                Chat chat = chatting.getContent().get(0);
                ChatRoomResponseDto.LatestMessage latestMessage = ChatRoomResponseDto.LatestMessage.builder()
                        .content(chat.getContent())
                        .sentAt(chat.getSentAt())
                        .build();
                dto.setLatestMessage(latestMessage);
            }
        });
        return chatRoomList;
    }

    // 유저가 입장하면 readCount를 0으로 업데이트
    public void updateCountZero(Long roomId, Member member){
        Update update = new Update().set("readCount",0);
        Query query = new Query(Criteria.where("roomId").is("roomId").and("senderId").ne(member.getMemberId()));
        mongoTemplate.updateMulti(query, update, Chat.class);
    }

    public void updateMessage(String email,Long roomId){
        Message message = Message.builder()
                .contentType("notice")
                .roomId(roomId)
                .content(email + "님이 돌아오셨습니다.")
                .build();

        kafkaProducer.sendChat(kafkaConstants.getTopic(), message);

    }


    @Transactional(readOnly = true)
    public RoomInfoDto getRoomInfo(Long roomId) {
        Room room = roomRepository.findByRoomId(roomId).orElseThrow(()-> new CustomException(ErrorCode.ROOM_NOT_EXIST));
        String createMember = memberRepository.findByMemberId(room.getBuyerId()).get().getNickname();
        String joinMember = memberRepository.findByMemberId(room.getSellerId()).get().getNickname();

        RoomInfoDto roomInfoDto =  RoomInfoDto.builder()
                .room(room)
                .joinMember(joinMember)
                .createMember(createMember)
                .build();

        return roomInfoDto;

    }

    public Message saveMessage(Message message) {
        Member member = memberRepository.findByMemberId(message.getSenderId()).orElseThrow(()->new CustomException(ErrorCode.NO_MEMBER_EXIST));
        Chat chat = message.convertEntity();
        Chat savedChat = mongoChatRepository.save(chat);
        message.setId(savedChat.getId());
        return message;
    }
}
