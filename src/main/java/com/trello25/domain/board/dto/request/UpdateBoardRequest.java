package com.trello25.domain.board.dto.request;

import com.trello25.domain.board.enums.BackColors;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateBoardRequest {

    @NotNull
    private  String title;
    private BackColors backColor;

}
