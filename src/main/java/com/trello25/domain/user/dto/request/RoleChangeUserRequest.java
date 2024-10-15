package com.trello25.domain.user.dto.request;

import com.trello25.domain.user.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoleChangeUserRequest {
        @NonNull
        private UserRole role;
}
