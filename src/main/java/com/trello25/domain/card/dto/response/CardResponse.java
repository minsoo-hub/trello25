package com.trello25.domain.card.dto.response;

import com.trello25.domain.card.entity.Card;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CardResponse {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime deadline;

    public CardResponse(Card card){
        this.id = card.getId();
        this.title = card.getTitle();
        this.description = card.getDescription();
        this.deadline = card.getDeadline();
    }
}
