package com.efub.bageasy.domain.chat.service;

import com.efub.bageasy.domain.chat.dto.ChatTopicDto;
import com.efub.bageasy.domain.chat.dto.Message;
import com.efub.bageasy.domain.member.domain.Member;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    @Value("${spring.kafka.topic")
    private String topic;
    private final KafkaTemplate<String, Message> kafkaTemplate;
    private final ChatRoomService chatRoomService;
    private final ObjectMapper mapper;

    public void sendChat(String topic, Message message){
//        boolean isConnectedAll = chatRoomService.isAllConnected(message.getRoomId());
        kafkaTemplate.send(topic, message);
    }

}
