package com.trello25.domain.workspace.controller;

import com.trello25.domain.auth.dto.AuthUser;
import com.trello25.domain.workspace.dto.UpdateWorkspaceRequest;
import com.trello25.domain.workspace.dto.WorkspaceRequest;
import com.trello25.domain.workspace.entity.Workspace;
import com.trello25.domain.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {
    private final WorkspaceService workspaceService;

    @PostMapping
    public ResponseEntity<Void> createWorkspace(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody WorkspaceRequest request
    ){
       return  workspaceService.create(authUser, request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Workspace> updateWorkspace(
            @PathVariable Long id,
            @RequestBody UpdateWorkspaceRequest request,
            @AuthenticationPrincipal AuthUser authUser
    ){
        return workspaceService.update(id, request, authUser);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Workspace> deleteWorkspace(  @PathVariable Long id,
                                                       @AuthenticationPrincipal AuthUser authUser){
        return workspaceService.delete(id, authUser);
    }

    @GetMapping
    public List<Workspace> getWorkspaces(){
        return workspaceService.getActiveWorkspace();
    }


}
