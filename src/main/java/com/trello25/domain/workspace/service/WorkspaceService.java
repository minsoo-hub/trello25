package com.trello25.domain.workspace.service;

import com.trello25.domain.auth.dto.AuthUser;
import com.trello25.domain.workspace.dto.UpdateWorkspaceRequest;
import com.trello25.domain.workspace.dto.WorkspaceRequest;
import com.trello25.domain.workspace.entity.Workspace;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface WorkspaceService {
    ResponseEntity<Void> create(AuthUser authUser, WorkspaceRequest request);
    ResponseEntity<Workspace> update(Long id, UpdateWorkspaceRequest request, AuthUser authUser);
    ResponseEntity<Workspace> delete(Long id, AuthUser authUser);
    List<Workspace>getActiveWorkspace();
}
