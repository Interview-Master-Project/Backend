package com.interview_master.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@AllArgsConstructor
@Getter
public enum ErrorCode {

    // USER
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "미인증 유저의 요청입니다."),

    // QUIZ
    QUIZ_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 질문은 존재하지 않습니다."),


    // COLLECTION
    COLLECTION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 컬렉션은 존재하지 않습니다."),

    // TOKEN
    AUTHORIZATION_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "로그인이 필요합니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "bad request");

    private final HttpStatus httpStatusCode;
    private final String description;
}
