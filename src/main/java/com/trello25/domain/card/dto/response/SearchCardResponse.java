package com.trello25.domain.card.dto.response;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class SearchCardResponse {

    private final Long id;
    private final String title;
    private final String description;
    private final LocalDate deadline;

    public SearchCardResponse(Long id, String title, String description, LocalDate deadline) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
    }
}
