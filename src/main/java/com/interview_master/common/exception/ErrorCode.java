package com.interview_master.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@AllArgsConstructor
@Getter
public enum ErrorCode {

    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "미인증 유저의 요청입니다."),

    ;

    private final HttpStatus httpStatusCode;
    private final String description;
}
