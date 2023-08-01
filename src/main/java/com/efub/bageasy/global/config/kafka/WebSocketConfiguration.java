package com.efub.bageasy.global.config.kafka;

import com.efub.bageasy.global.AgentWebSocketHandlerDecoratorFactory;
import com.efub.bageasy.global.config.interceptor.StompHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
    private final StompHandler stompHandler;
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(("/chat"))
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic/");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler); //stomp 메시지 핸들링
    }

    // 이미지 전송을 위해 메시지 크기 제한 늘림
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setMessageSizeLimit(50 * 1024 * 1024);
        registry.setSendBufferSizeLimit(1024 * 1024 * 50);
//        registry.setSendTimeLimit(20000);
        registry.setDecoratorFactories(agentWebSocketHandlerDecoratorFactory());
    }

    @Bean
    public AgentWebSocketHandlerDecoratorFactory agentWebSocketHandlerDecoratorFactory(){
        return new AgentWebSocketHandlerDecoratorFactory();
    }
}
