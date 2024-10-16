package com.trello25.domain.kanban.dto.response;

import com.trello25.domain.card.dto.response.CardResponse;
import com.trello25.domain.kanban.entity.Kanban;
import lombok.Getter;

@Getter
public class KanbanResponse {

    private final long id;
    private final String title;
    private final CardResponse cardResponse;

    public KanbanResponse(Kanban kanban, CardResponse cardResponse) {
        this.id = kanban.getId();
        this.title = kanban.getTitle();
        this.cardResponse = cardResponse;
    }
}
