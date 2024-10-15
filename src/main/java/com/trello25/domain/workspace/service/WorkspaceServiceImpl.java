package com.trello25.domain.workspace.service;

import com.trello25.domain.workspace.dto.UpdateWorkspaceRequest;
import com.trello25.domain.workspace.dto.WorkspaceRequest;
import com.trello25.domain.workspace.entity.Workspace;
import com.trello25.domain.workspace.repository.WorkspaceRepository;
import com.trello25.exception.ApplicationException;
import com.trello25.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepository;

    /**
     * workspace 생성
     * @param request
     * @return statuscode 200
     */
    public ResponseEntity<Void> create(WorkspaceRequest request){
        //권한 ADMIN인지 확인하는 logic 필요
    Workspace Workspace = new Workspace(
            request.getTitle(),
            request.getDescription()
    );
      workspaceRepository.save(Workspace);
      return ResponseEntity.ok().build();
    }

    public ResponseEntity<Workspace> update(Long id, UpdateWorkspaceRequest request){
        Workspace workspace = workspaceRepository.findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.WORKSPACE_NOT_FOUND));

        workspace.update(request.getTitle(), request.getDescription());
        Workspace saveWorkspace = workspaceRepository.save(workspace);
        return ResponseEntity.ok().body(saveWorkspace);
    }
}


