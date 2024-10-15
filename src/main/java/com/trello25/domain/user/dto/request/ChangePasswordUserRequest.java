package com.trello25.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordUserRequest {
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;
}
