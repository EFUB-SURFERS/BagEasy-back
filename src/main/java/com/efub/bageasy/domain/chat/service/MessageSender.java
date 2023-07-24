package com.efub.bageasy.domain.chat.service;

import com.efub.bageasy.domain.chat.domain.Chat;
import com.efub.bageasy.domain.chat.dto.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageSender {
    private final KafkaTemplate<String, Message> kafkaTemplate;

    public void send(String topic, Message data){
        kafkaTemplate.send(topic, data);
    }
}
