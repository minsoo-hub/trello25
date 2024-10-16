package com.trello25.domain.workspace.controller;

import com.trello25.domain.workspace.dto.UpdateWorkspaceRequest;
import com.trello25.domain.workspace.dto.WorkspaceRequest;
import com.trello25.domain.workspace.entity.Workspace;
import com.trello25.domain.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {
    private final WorkspaceService workspaceService;

    @PostMapping
    public ResponseEntity<Void> createWorkspace(
            //token나오면 principle
            @RequestBody WorkspaceRequest request
    ){
        ResponseEntity<Void> result = workspaceService.create(request);
       return  ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Workspace> updateWorkspace(
            @PathVariable Long id,
            @RequestBody UpdateWorkspaceRequest request
    ){
        return workspaceService.update(id, request);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Workspace> deleteWorkspace(  @PathVariable Long id){
        return workspaceService.delete(id);
    }
}
