package com.trello25.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    EMPTY_BOARD_TITLE(HttpStatus.BAD_REQUEST, "보드 제목을 입력해야 합니다."),
    INVALID_USER_ROLE(HttpStatus.BAD_REQUEST, "유효하지 않은 UserRole입니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "잘못된 비밀번호입니다."),
    SAME_OLD_PASSWORD(HttpStatus.BAD_REQUEST, "새 비밀번호는 기존 비밀번호와 같을 수 없습니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 JWT 토큰입니다."),
    INVALID_AUTH_REQUEST(HttpStatus.BAD_REQUEST, "유효하지 않은 인증 요청입니다."),

    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "잘못된 비밀번호입니다."),


    FORBIDDEN_ACTION(HttpStatus.FORBIDDEN, "해당 작업을 수행할 권한이 없습니다."),
    FORBIDDEN_READ_ONLY_USER(HttpStatus.FORBIDDEN,"읽기 전용 권한의 사용자는 보드를 생성할 수 없습니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "권한이 없습니다."),

    INVALID_KANBAN_POSITION(HttpStatus.NOT_FOUND, "유효하지 않은 칸반 위치입니다."),
    WORKSPACE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 workspace입니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 보드입니다."),
    KANBAN_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 칸반입니다."),
    KANBAN_POSITION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 칸반 순서입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND,"멤버를 찾을 수 없습니다."),
    INVALID_PERMISSION(HttpStatus.NOT_FOUND,"올바른 Permission을 입력해주세요"),
    CARD_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 카드입니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다."),

    EMAIL_DUPLICATED(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다.");


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
