package com.trello25.domain.kanban.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateKanbanPositionRequest {

    @NotNull(message = "칸반 순서는 필수 입력 항목입니다.")
    private final Integer position;

    @JsonCreator
    public UpdateKanbanPositionRequest(int position) {
        this.position = position;
    }
}
