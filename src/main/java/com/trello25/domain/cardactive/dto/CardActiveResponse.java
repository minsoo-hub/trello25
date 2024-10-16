package com.trello25.domain.cardactive.dto;

import com.trello25.domain.cardactive.actiontype.ActionType;
import lombok.Getter;

@Getter
public class CardActiveResponse {

    private final Long id;
    private final Long memberId;
    private final ActionType action;

    public CardActiveResponse(Long id, Long memberId, ActionType action) {
        this.id = id;
        this.memberId = memberId;
        this.action = action;
    }
}
