package com.trello25.domain.kanban.dto.response;

import com.trello25.domain.card.dto.response.CardResponse;
import com.trello25.domain.kanban.entity.Kanban;
import java.util.List;
import lombok.Getter;

@Getter
public class KanbanResponse {

    private final long id;
    private final String title;
    private final List<CardResponse> cards;

    public KanbanResponse(Kanban kanban, List<CardResponse> cardResponses) {
        this.id = kanban.getId();
        this.title = kanban.getTitle();
        this.cards = cardResponses;
    }
}