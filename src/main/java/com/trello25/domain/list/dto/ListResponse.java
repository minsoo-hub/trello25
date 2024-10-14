package com.trello25.domain.list.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ListResponse {

    private final long id;
    private final String title;
    private final int order;
}
