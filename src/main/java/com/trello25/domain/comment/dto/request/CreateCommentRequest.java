package com.trello25.domain.comment.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateCommentRequest {

    @NotNull(message = "카드 ID는 필수 입력 항목입니다.")
    private final Long cardId;

    @NotBlank(message = "댓글 내용은 필수 입력 항목입니다.")
    private final String content;

    @JsonCreator
    public CreateCommentRequest(Long cardId, String content) {
        this.cardId = cardId;
        this.content = content;
    }
}
