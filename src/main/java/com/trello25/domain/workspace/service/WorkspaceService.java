package com.trello25.domain.workspace.service;

import com.trello25.domain.workspace.dto.WorkspaceRequest;
import org.springframework.http.ResponseEntity;

public interface WorkspaceService {
    ResponseEntity<Void> create(WorkspaceRequest request);
}
