package com.efub.bageasy.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class SwaggerExampleReqDto {
    @Schema(description = "학번")
    public String studentNo;
}
