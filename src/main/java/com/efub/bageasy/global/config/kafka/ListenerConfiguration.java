package com.efub.bageasy.global.config.kafka;

import com.efub.bageasy.domain.chat.dto.Message;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;


import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class ListenerConfiguration {
    private final KafkaConstants kafkaConstants;

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, Message> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Message> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        return factory;
    }


    @Bean
    public ConsumerFactory<String, Message> consumerFactory(){
        JsonDeserializer<Message> deserializer = new JsonDeserializer<>();
        deserializer.addTrustedPackages("*");

        Map<String, Object> configurations = new HashMap<>();

        configurations.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConstants.getBroker());
        configurations.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConstants.getGroupId());
        configurations.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configurations.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        configurations.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        return new DefaultKafkaConsumerFactory<>(configurations, new StringDeserializer(), deserializer);
    }
}
