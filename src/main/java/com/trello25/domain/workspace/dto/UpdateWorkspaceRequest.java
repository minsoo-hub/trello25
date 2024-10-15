package com.trello25.domain.workspace.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateWorkspaceRequest {
    @NotNull(message = "workspace이름을 입력해주세요")
    private String title;
    @NotNull(message = "workspace를 설명해주세요")
    private String description;
}
