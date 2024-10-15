package com.trello25.domain.workspace.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class addMembersRequest {
        @NotNull(message = "email을 입력해주세요")
        private String email;

}
