package com.efub.bageasy.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SwaggerExampleResDto {
    @Schema(description = "이름")
    private String name;
    @Schema(description = "나이")
    private int age;

}
