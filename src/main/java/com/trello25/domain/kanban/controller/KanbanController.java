package com.trello25.domain.kanban.controller;

import com.trello25.domain.kanban.AuthUser;
import com.trello25.domain.kanban.dto.request.CreateKanbanRequest;
import com.trello25.domain.kanban.service.KanbanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KanbanController {

    private final KanbanService kanbanService;

    @PostMapping("/kanbans")
    public ResponseEntity<Void> createKanban(
            @RequestBody CreateKanbanRequest request
    ) {
        // TODO: 로그인 코드 완성되면 수정 필요

        AuthUser authUser = new AuthUser();
        kanbanService.createKanban(authUser, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }
}
