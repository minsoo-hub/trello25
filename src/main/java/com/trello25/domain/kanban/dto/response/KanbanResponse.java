package com.trello25.domain.kanban.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KanbanResponse {

    private final long id;
    private final String title;
}
