package com.interview_master.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

  // COMMON
  NULL_EXCEPTION(HttpStatus.BAD_REQUEST, "해당 값이 null 입니다."),
  FORBIDDEN_MODIFICATION(HttpStatus.FORBIDDEN, "수정 권한이 없습니다."),
  SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러 입니다."),
  USER_MISMATCH(HttpStatus.BAD_REQUEST, "사용자 정보가 일치하지 않습니다."),

  // USER
  UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "미인증 유저의 요청입니다."),
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저는 존재하지 않습니다."),

  // QUIZ
  QUIZ_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 질문은 존재하지 않습니다."),

  // COLLECTION
  COLLECTION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 컬렉션은 존재하지 않습니다."),

  // USER_COLLECTION_ATTEMPT
  USER_COLLECTION_ATTEMPT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 컬렉션 시도 기록은 존재하지 않습니다."),

  // TOKEN
  AUTHORIZATION_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "로그인이 필요합니다."),
  INVALID_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 로그인 입니다."),
  BAD_REQUEST(HttpStatus.BAD_REQUEST, "bad request"),

  // ACCESS
  FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "접근 권한이 없습니다!"),

  // NCP_BUCKET
  EMPTY_FILE_EXCEPTION(HttpStatus.BAD_REQUEST, "빈 파일입니다."),
  NO_FILE_EXTENTION(HttpStatus.BAD_REQUEST, "확장자가 없습니다."),
  INVALID_FILE_EXTENTION(HttpStatus.BAD_REQUEST, "부적절한 이미지 확장자입니다."),
  IO_EXCEPTION_ON_IMAGE_UPLOAD(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드 중 에러가 발생했습니다."),
  PUT_OBJECT_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "S3에 이미지 업로드 중 에러가 발생했습니다."),
  IO_EXCEPTION_ON_IMAGE_DELETE(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 삭제 중 에러가 발생했습니다."),
  SAVE_IMAGE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DB에 이미지 정보 저장에 실패했습니다."),

  // CATEGORY
  CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 카테고리는 존재하지 않습니다."),

  // SORT
  UNSUPPORTED_SORT_ORDER(HttpStatus.BAD_REQUEST, "지원하지 않는 정렬 조건입니다."),

  // COLLECTION_LIKE
  COLLECTION_LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 컬렉션에 대한 좋아요 기록은 존재하지 않습니다."),
  ALREADY_LIKED(HttpStatus.BAD_REQUEST, "이미 좋아요한 게시물입니다."),
  ALREADY_UNLIKED(HttpStatus.BAD_REQUEST, "이미 좋아요 취소한 게시물입니다."),
  ;

  private final HttpStatus httpStatusCode;
  private final String description;
}
