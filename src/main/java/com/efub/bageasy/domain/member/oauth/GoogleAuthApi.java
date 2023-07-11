package com.efub.bageasy.domain.member.oauth;

import com.efub.bageasy.domain.member.dto.GoogleRequestAccessTokenDto;
import com.efub.bageasy.global.config.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "googleAuth", url="https://oauth2.googleapis.com", configuration = {FeignConfiguration.class})
public interface GoogleAuthApi {
    @PostMapping("/token")
    ResponseEntity<String> getAccessToken(@RequestBody GoogleRequestAccessTokenDto requestDto);
}