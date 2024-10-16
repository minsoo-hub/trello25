package com.trello25.domain.workspace.service;

import com.trello25.domain.auth.dto.AuthUser;
import com.trello25.domain.user.entity.User;
import com.trello25.domain.user.enums.UserRole;
import com.trello25.domain.workspace.dto.UpdateWorkspaceRequest;
import com.trello25.domain.workspace.dto.WorkspaceRequest;
import com.trello25.domain.workspace.entity.Workspace;
import com.trello25.domain.workspace.repository.WorkspaceRepository;
import com.trello25.exception.ApplicationException;
import com.trello25.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.trello25.domain.user.enums.UserRole.USER;

@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepository;

    /**
     * workspace 생성
     *
     * @param request
     * @return statuscode 200
     */
    //todo : 모든 API ADMIN 권한 확인 필요 !!
    public ResponseEntity<Void> create(AuthUser authUser, WorkspaceRequest request) {
        //권한 ADMIN인지 확인하는 logic 필요
        checkIfUserIsAuthorized(authUser);

        Workspace Workspace = new Workspace(
                request.getTitle(),
                request.getDescription()
        );
        workspaceRepository.save(Workspace);
        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity<Workspace> update(Long id, UpdateWorkspaceRequest request, AuthUser authUser) {
        checkIfUserIsAuthorized(authUser);
        Workspace workspace = workspaceRepository.findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.WORKSPACE_NOT_FOUND));

        workspace.update(request.getTitle(), request.getDescription());
        //Workspace saveWorkspace = workspaceRepository.save(workspace);
        return ResponseEntity.ok().body(workspaceRepository.save(workspace));
    }

    public ResponseEntity<Workspace> delete(Long id, AuthUser authUser) {
        checkIfUserIsAuthorized(authUser);
        Workspace workspace = workspaceRepository.findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.WORKSPACE_NOT_FOUND));
        workspace.delete();
        Workspace saveWorkspace = workspaceRepository.save(workspace);
        return ResponseEntity.ok().body(saveWorkspace);
    }

    public ResponseEntity<Void> getAllSpace() {
        Workspace workspace = workspaceRepository.findAllSpace();

    }

    public void checkIfUserIsAuthorized(AuthUser authUser) {
        if (authUser.getUserRole() == UserRole.USER) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED);
        }
    }

}


