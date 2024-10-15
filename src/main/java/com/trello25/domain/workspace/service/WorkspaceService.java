package com.trello25.domain.workspace.service;

import com.trello25.domain.workspace.dto.UpdateWorkspaceRequest;
import com.trello25.domain.workspace.dto.WorkspaceRequest;
import com.trello25.domain.workspace.entity.Workspace;
import org.springframework.http.ResponseEntity;

public interface WorkspaceService {
    ResponseEntity<Void> create(WorkspaceRequest request);
    ResponseEntity<Workspace> update(Long id, UpdateWorkspaceRequest request);
    ResponseEntity<Workspace> delete(Long id);
}
