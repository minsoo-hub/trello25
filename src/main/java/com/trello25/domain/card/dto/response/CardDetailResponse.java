package com.trello25.domain.card.dto.response;

import com.trello25.domain.card.entity.Card;
import com.trello25.domain.cardactive.dto.CardActiveResponse;
import com.trello25.domain.comment.dto.CommentResponse;
import java.util.List;
import lombok.Getter;

@Getter
public class CardDetailResponse {

    private final Long id;
    private final String title;
    private final String description;
    private final List<CardActiveResponse> cardActives;
    private final List<CommentResponse> comments;

    public CardDetailResponse(Card card, List<CardActiveResponse> cardActives, List<CommentResponse> comments) {
        this.id = card.getId();
        this.title = card.getTitle();
        this.description = card.getDescription();
        this.cardActives = cardActives;
        this.comments = comments;
    }
}
