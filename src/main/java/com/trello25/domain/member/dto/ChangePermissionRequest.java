package com.trello25.domain.member.dto;
import com.trello25.domain.member.entity.Permission;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePermissionRequest {
    @NotNull(message="permission을 입력해주세요")
    private Permission permission;
}
