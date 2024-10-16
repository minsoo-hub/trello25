package com.trello25.domain.cardactive.actiontype;


public enum ActionType {
    CREATE("카드 생성"),
    UPDATE("카드 업데이트"),
    DELETE("카드 삭제"),
    ADD_COMMENT("댓글 추가"),
    REMOVE_COMMENT("댓글 삭제"),
    ASSIGN_MANAGER("매니저 할당");

    private final String description;

    ActionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
