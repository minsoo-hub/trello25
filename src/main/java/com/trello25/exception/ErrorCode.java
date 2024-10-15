package com.trello25.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    EMAIL_DUPLICATED(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    WORKSPACE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 workspace입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(final HttpStatus httpStatus, final String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public int getStatusCode() {
        return httpStatus.value();
    }

    public String getMessage() {
        return message;
    }
}
