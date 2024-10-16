package com.trello25.domain.user.dto.response;

import lombok.Getter;

@Getter
public class SaveUserResponse {

    private final String bearerToken;

    public SaveUserResponse(String bearerToken) {
        this.bearerToken = bearerToken;
    }
}
