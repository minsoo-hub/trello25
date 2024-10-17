package com.trello25.domain.comment.dto;

import lombok.Getter;

@Getter
public class CommentResponse {

    private final Long id;
    private final String text;

    public CommentResponse(Long id,String text) {
        this.id = id;
        this.text = text;
    }
}
