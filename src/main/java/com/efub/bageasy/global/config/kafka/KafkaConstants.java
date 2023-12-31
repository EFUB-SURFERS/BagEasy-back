package com.efub.bageasy.global.config.kafka;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "kafka.bageasy")
public class KafkaConstants {
    private String topic;
    private String groupId;
    private String broker;
}
