package com.trello25.domain.user.dto.response;

import com.trello25.domain.user.enums.UserRole;
import lombok.Getter;

@Getter
public class UserResponse {

    private final Long id;
    private final String email;
    private final UserRole userRole;

    public UserResponse(Long id, String email, UserRole userRole) {
        this.id = id;
        this.email = email;
        this.userRole = userRole;
    }
}