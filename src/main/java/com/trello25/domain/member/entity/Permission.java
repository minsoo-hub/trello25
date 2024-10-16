package com.trello25.domain.member.entity;

public enum Permission {
    WORKSPACE_MEMBER,  // 워크스페이스 멤버 (기본 역할)
    BOARD_MEMBER,      // 보드 멤버 (보드 관련 기능 사용 가능)
    READ_ONLY
}
