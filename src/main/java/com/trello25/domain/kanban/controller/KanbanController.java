package com.trello25.domain.kanban.controller;

import com.trello25.domain.auth.dto.AuthUser;
import com.trello25.domain.kanban.dto.request.CreateKanbanRequest;
import com.trello25.domain.kanban.dto.request.UpdateKanbanPositionRequest;
import com.trello25.domain.kanban.dto.request.UpdateKanbanTitleRequest;
import com.trello25.domain.kanban.dto.response.KanbanResponse;
import com.trello25.domain.kanban.service.KanbanService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KanbanController {

    private final KanbanService kanbanService;

    @PostMapping("/kanbans")
    public ResponseEntity<Void> createKanban(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody CreateKanbanRequest request
    ) {
        kanbanService.createKanban(authUser, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @DeleteMapping("/kanbans/{id}")
    public ResponseEntity<Void> deleteKanban(@AuthenticationPrincipal AuthUser authUser, @PathVariable long id) {
        kanbanService.deleteKanban(authUser, id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PatchMapping("/kanbans/{id}/title")
    public ResponseEntity<Void> updateKanbanTitle(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable long id,
            @RequestBody UpdateKanbanTitleRequest request
    ) {
        kanbanService.updateKanbanTitle(authUser, id, request);

        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @PatchMapping("/kanbans/{id}/position")
    public ResponseEntity<Void> updateKanbanPosition(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable long id,
            @RequestBody UpdateKanbanPositionRequest request
    ) {
        kanbanService.updateKanbanPosition(authUser, id, request);

        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }
}
