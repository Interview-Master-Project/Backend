package com.interview_master.common.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

  private final ErrorCode errorCode;
  private final String description;

  public ApiException(ErrorCode errorCode) {
    this.errorCode = errorCode;
    this.description = errorCode.getDescription();
  }

  public ApiException(ErrorCode errorCode, String message) {
    this.errorCode = errorCode;
    this.description = message;
  }

  public ApiException(Throwable ex, ErrorCode errorCode) {
    super(ex);
    this.errorCode = errorCode;
    this.description = errorCode.getDescription();
  }
}
