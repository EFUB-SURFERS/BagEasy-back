package com.efub.bageasy.domain.chat.service;

import com.efub.bageasy.domain.chat.dto.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumer {
    private final SimpMessageSendingOperations template;

    @KafkaListener(topics = "${kafka.bageasy.topic}", containerFactory = "kafkaListenerContainerFactory") //kafkaListerContainerFactory of ListenerConfiguration.class
    public void receiveMessage(Message message){
        log.info("전송 위치 = /topic/group/"+ message.getRoomId());
        log.info("채팅 방으로 메시지 전송 = {}", message);
        template.convertAndSend("/topic/group/" + message.getRoomId(), message);
    }


}
