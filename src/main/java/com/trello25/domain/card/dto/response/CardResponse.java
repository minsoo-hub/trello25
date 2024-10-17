package com.trello25.domain.card.dto.response;

import com.trello25.domain.card.entity.Card;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CardResponse {

    private final Long id;
    private final String title;
    private final String description;
    private final LocalDate deadline;

    public CardResponse(Card card) {
        this.id = card.getId();
        this.title = card.getTitle();
        this.description = card.getDescription();
        this.deadline = card.getDeadline();
    }
}