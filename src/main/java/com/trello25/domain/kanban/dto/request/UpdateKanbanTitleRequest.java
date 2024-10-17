package com.trello25.domain.kanban.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateKanbanTitleRequest {

    @NotBlank(message = "칸반 제목은 필수 입력 항목입니다.")
    private final String title;

    @JsonCreator
    public UpdateKanbanTitleRequest(String title) {
        this.title = title;
    }
}
