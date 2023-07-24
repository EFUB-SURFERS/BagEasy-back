package com.efub.bageasy.domain.member.dto.response;

import lombok.Getter;

@Getter
public class TokenDto {
    private String accessToken;
    private String refreshToken;

    public TokenDto(String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
