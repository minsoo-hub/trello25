package com.trello25.domain.comment.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateCommentContentRequest {

    @NotBlank(message = "댓글 내용은 필수 입력 항목입니다.")
    private final String content;

    @JsonCreator
    public UpdateCommentContentRequest(String content) {
        this.content = content;
    }
}
