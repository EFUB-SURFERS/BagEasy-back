package com.efub.bageasy.domain.member.controller;

import com.efub.bageasy.domain.member.dto.request.SwaggerExampleReqDto;
import com.efub.bageasy.domain.member.dto.response.SwaggerExampleResDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@Tag(name ="멤버", description = "멤버 관련 api입니다.")
public class SwaggerExample {

    @Operation(summary = "swagger 테스트용 api", description = "api에 대한 설명을 작성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = SwaggerExampleResDto.class))),
//            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = SwaggerExampleResDto.class)))
    })
    @PostMapping("/example")
    public ResponseEntity<SwaggerExampleResDto> testSwagger(@RequestBody SwaggerExampleReqDto request){
        SwaggerExampleResDto response = new SwaggerExampleResDto("name", 22);
        return ResponseEntity.ok().body(response);
    }

}
