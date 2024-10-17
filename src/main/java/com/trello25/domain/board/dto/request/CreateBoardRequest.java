package com.trello25.domain.board.dto.request;

import com.trello25.domain.board.enums.BackColors;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateBoardRequest {

    @NotBlank(message = "제목을 입력해주세요")
    private  String title;
    //    @NotBlank(message = "배경색을 선택해주세요")
    private BackColors backColor;

}