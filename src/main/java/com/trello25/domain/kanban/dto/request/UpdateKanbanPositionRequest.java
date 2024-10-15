package com.trello25.domain.kanban.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class UpdateKanbanPositionRequest {

    private final int position;

    @JsonCreator
    public UpdateKanbanPositionRequest(int position) {
        this.position = position;
    }
}
