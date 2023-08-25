package com.efub.bageasy.domain.chat.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.efub.bageasy.domain.chat.domain.Chat;
import com.efub.bageasy.domain.chat.domain.Room;
import com.efub.bageasy.domain.chat.dto.Message;
import com.efub.bageasy.domain.chat.dto.request.RoomCreateRequest;
import com.efub.bageasy.domain.chat.dto.response.*;
import com.efub.bageasy.domain.chat.mongo.MongoChatRepository;
import com.efub.bageasy.domain.chat.repository.ChatQuerydslRepository;
import com.efub.bageasy.domain.chat.repository.RoomQuerydslRepository;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final PostRepository postRepository;
    private final RoomRepository roomRepository;
    private final RoomQuerydslRepository roomQuerydslRepository;
    private final MemberRepository memberRepository;
    private final MongoChatRepository mongoChatRepository;
    private final ChatQuerydslRepository chatQuerydslRepository;
    private final KafkaProducer kafkaProducer;
    private final KafkaConstants kafkaConstants;
    private final JwtTokenProvider tokenProvider;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public RoomCreateResponse makeChatRoom(Member member, RoomCreateRequest roomCreateRequest) {
        Post post = postRepository.findPostByPostId(roomCreateRequest.getPostId()).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        //판매완료된 게시글
        if(post.getIsSold()){
            throw new CustomException(ErrorCode.POST_SOLDOUT);
        }

        //존재하지 않는 멤버 아이디
        Member createMember = memberRepository.findMemberByNickname(member.getNickname())
                .orElseThrow(() -> new CustomException(ErrorCode.NO_MEMBER_EXIST));

        //이미 존재하는 채팅방
        Long roomCount = roomQuerydslRepository.countExistingRoom(member.getMemberId(), post.getMemberId(), post.getPostId());
        if(roomCount!= null){
            throw new CustomException(ErrorCode.ROOM_ALREADY_EXIST, roomCount.toString() );
        }

        //자기자신을 채팅방에 초대하는 경우
        if(member.getMemberId().equals(post.getMemberId())){
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

    public void sendMessage(Message message, String token) throws IOException {
//        boolean isConnectAll = chatRoomService.isAllConnected(message.getRoomId());
//        Integer readCount = isConnectAll ? 0 : 1;

        //메세지 발신자 닉네임과 발신시간 설정
        String nickname = tokenProvider.getNicknameFromToken(token);
        message.setSendTimeAndSender(LocalDateTime.now(), nickname);

        //메세지 타입이 이미지인 경우 (type == 1)
        if (message.getType() == 1) {
            String[] strings = message.getContent().split(",");

            String base64Image = strings[1];

            //파일 확장자 결정
            String extension = "";
            if (strings[0].equals("data:image/jpeg;base64")) {
                extension = "jpeg";
            } else if (strings[0].equals("data:image/png;base64")){
                extension = "png";
            } else {
                extension = "jpg";
            }

            byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
            File tempFile = File.createTempFile("image", "." + extension);

            try (OutputStream outputStream = new FileOutputStream(tempFile)) {
                outputStream.write(imageBytes);
            }

            String fileName = message.getNickname()+'/'+(UUID.randomUUID().toString());

            //s3 업로드
            amazonS3Client.putObject(new PutObjectRequest(bucket + "/chat/image", fileName, tempFile).withCannedAcl(CannedAccessControlList.PublicRead));
            String uploadedImg = amazonS3Client.getUrl(bucket + "/chat/image", fileName).toString();

            //temp 파일 삭제
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
                fileOutputStream.close(); // 아웃풋 닫아주기
                if (tempFile.delete()) {
                    log.info("File delete success");
                } else {
                    log.info("File delete fail");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            message.setImageUrl(uploadedImg);
        }

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

        List<Chat> chats = mongoChatRepository.findByRoomIdOrderBySentAt(roomId);

        updateReadState(chats, member);

        List<ChatResponseDto> chatList = chats
                .stream()
                .map(e -> new ChatResponseDto(e,member.getNickname()))
                .sorted(Comparator.comparing(ChatResponseDto::getSentAt))
                .collect(Collectors.toList());

        return ChatRoomRecordDto.builder()
                .chatList(chatList)
                .nickname(member.getNickname().equals(createMemNickname)?joinMemNickname:createMemNickname)
                .build();
    }

    public void updateReadOnDisconnect(Long roomId, String nickname){
        Member member = memberRepository.findMemberByNickname(nickname).orElseThrow(()-> new CustomException(ErrorCode.NO_MEMBER_EXIST));
        List<Chat> chats = mongoChatRepository.findByRoomIdOrderBySentAt(roomId);
        updateReadState(chats, member);
    }

    public List<ChatRoomResponseDto> getChatRoomList(Member member) {
        List<Room> chatRooms = chatQuerydslRepository.getChatRooms(member.getMemberId());

        List<ChatRoomResponseDto> chatRoomList = new ArrayList<>();

        for(Room room : chatRooms){
            Page<Chat> chatting = mongoChatRepository.findByRoomIdOrderBySentAtDesc(room.getRoomId(), PageRequest.of(0, 1));
            if (chatting.hasContent()) {
                Chat chat = chatting.getContent().get(0);
                ChatRoomResponseDto.LatestMessage latestMessage = ChatRoomResponseDto.LatestMessage.builder()
                        .content(chat.getType()==1 ? "(사진)" : chat.getContent())
                        .sentAt(chat.getSentAt())
                        .isRead(chat.getIsRead())
                        .isMine(member.getNickname().equals(chat.getNickname()))
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

        chatRoomList.sort(Comparator.comparing(ChatRoomResponseDto::getLatestMessage, Comparator.comparing(ChatRoomResponseDto.LatestMessage::getSentAt)).reversed());

        return chatRoomList;
    }

    public void updateMessage(String nickname,Long roomId){
        Message message = Message.builder()
                .contentType("notice")
                .roomId(roomId)
                .content(nickname + "님이 돌아오셨습니다.")
                .build();

        kafkaProducer.sendChat(kafkaConstants.getTopic(), message);

    }

    public void updateReadState(List<Chat> chats, Member member){
        if(chats.size()!= 0){
            Chat latestMessage = chats.get(chats.size() - 1);

            //가장 마지막 채팅 발신자가 아닌 사람이 채팅 목록을 조회하면, 가장 마지막 채팅의 isRead 필드를 true로 변경
            if(!member.getNickname().equals(latestMessage.getNickname())){
                latestMessage.setIsRead(true);
            }

            mongoChatRepository.save(latestMessage);
        }
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
            Chat savedChat = mongoChatRepository.save(chat);
            message.setId(savedChat.getId());
        }
        return message;
    }
}
