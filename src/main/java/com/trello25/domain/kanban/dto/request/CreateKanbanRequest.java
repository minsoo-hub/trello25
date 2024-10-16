package com.trello25.domain.kanban.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateKanbanRequest {

    @NotNull(message = "보드 ID는 필수 입력 항목입니다.")
    private final Long boardId;

    @NotBlank(message = "제목은 필수 입력 항목입니다.")
    private final String title;
}
