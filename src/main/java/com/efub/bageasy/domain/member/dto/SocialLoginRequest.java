package com.efub.bageasy.domain.member.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class SocialLoginRequest {
    @NotNull
    private String code;
}