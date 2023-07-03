package com.efub.bageasy.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI(){
        Info info = new Info()
                .title("데모 프로젝트 API Document")
                .version("v0.0.1")
                .description("데모 프로젝트의 API 명세서입니다.");

        // SecuritySchemes 내용 작성 후 return하는 OpenAPI 객체에 추가해주세요.
        // 이후 BageasyApplication.class에서 (exclude = SecurityAutoConfiguration.class) 부분 삭제해주세요.

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
