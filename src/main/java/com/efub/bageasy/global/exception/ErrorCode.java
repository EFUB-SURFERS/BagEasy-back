package com.efub.bageasy.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    //400
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "토큰이 유효하지 않습니다."), //예시
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "토큰이 만료되었습니다."),  //예시

    NON_LOGIN(HttpStatus.UNAUTHORIZED, "로그인 후 이용 가능합니다."),

    INVALID_ACCESS(HttpStatus.BAD_REQUEST,"올바르지 않은 접근입니다. 헤더를 확인해주세요."),

    NO_MEMBER_EXIST(HttpStatus.BAD_REQUEST, "가입되지 않은 회원입니다."),

    DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),
    HEART_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 하트입니다"),

    FILE_CONVERT_ERROR(HttpStatus.BAD_REQUEST,"파일 전환에 실패하였습니다."),
    FILE_UPLOAD_ERROR(HttpStatus.BAD_REQUEST,"이미지 업로드에 실패하였습니다."),
    FILE_DELETE_ERROR(HttpStatus.BAD_REQUEST,"이미지 삭제에 실패하였습니다.");


    private final HttpStatus status;
    private final String message;
}