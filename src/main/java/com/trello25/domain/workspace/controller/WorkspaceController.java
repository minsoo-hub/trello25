package com.trello25.domain.workspace.controller;

import com.trello25.domain.auth.dto.AuthUser;
import com.trello25.domain.workspace.dto.UpdateWorkspaceRequest;
import com.trello25.domain.workspace.dto.WorkspaceRequest;
import com.trello25.domain.workspace.entity.Workspace;
import com.trello25.domain.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {
    private final WorkspaceService workspaceService;

    @PostMapping
    public ResponseEntity<Void> createWorkspace(
            AuthUser authUser,
            @RequestBody WorkspaceRequest request
    ){
        System.out.println(authUser.getUserRole());
        workspaceService.create(authUser, request);
       return  ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Workspace> updateWorkspace(
            @PathVariable Long id,
            @RequestBody UpdateWorkspaceRequest request,
            AuthUser authUser
            
    ){
        return workspaceService.update(id, request, authUser);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Workspace> deleteWorkspace(  @PathVariable Long id, AuthUser authUser){
        return workspaceService.delete(id, authUser);
    }

    @GetMapping
    public List<Workspace> getWorkspaces(){
        return workspaceService.getActiveWorkspace();
    }


}
