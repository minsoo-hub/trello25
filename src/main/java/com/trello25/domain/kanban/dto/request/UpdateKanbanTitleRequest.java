package com.trello25.domain.kanban.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class UpdateKanbanTitleRequest {

    private final String title;

    @JsonCreator
    public UpdateKanbanTitleRequest(String title) {
        this.title = title;
    }
}
