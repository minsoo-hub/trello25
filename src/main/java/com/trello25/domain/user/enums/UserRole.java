package com.trello25.domain.user.enums;


import com.trello25.exception.ApplicationException;
import com.trello25.exception.ErrorCode;

import java.util.Arrays;

public enum UserRole {
    ADMIN, USER;

    public static UserRole of(String role) {
        return Arrays.stream(UserRole.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_USER_ROLE));
    }
}
