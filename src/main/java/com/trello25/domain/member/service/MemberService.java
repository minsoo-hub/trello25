package com.trello25.domain.member.service;

import com.trello25.domain.auth.dto.AuthUser;
import com.trello25.domain.common.entity.EntityStatus;
import com.trello25.domain.member.dto.ChangePermissionRequest;
import com.trello25.domain.member.entity.Member;
import com.trello25.domain.member.entity.Permission;
import com.trello25.domain.member.repository.MemberRepository;
import com.trello25.domain.user.entity.User;
import com.trello25.domain.user.enums.UserRole;
import com.trello25.domain.user.repository.UserRepository;
import com.trello25.domain.workspace.entity.Workspace;
import com.trello25.domain.workspace.repository.WorkspaceRepository;
import com.trello25.exception.ApplicationException;
import com.trello25.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;

    public Member addMembers(AuthUser authUser, Long workspaceId, Long userId) {

        Member member = memberRepository.findMemberIdsByUserId(authUser.getId());
        System.out.println("status = " + member);

        if (member.getPermission() != Permission.WORKSPACE_MEMBER ) throw new ApplicationException(ErrorCode.UNAUTHORIZED_ACCESS);

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.WORKSPACE_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));


      //  Member member = new Member();
        member.setWorkspace(workspace);
        member.setUser(user);
        member.setPermission(Permission.BOARD_MEMBER);  // Default permission

        return memberRepository.save(member);
    }

    public ResponseEntity<Member> changePermission(AuthUser authUser,Long workspaceId, Long id, ChangePermissionRequest request) {
        checkIfUserIsAuthorized(authUser);

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.WORKSPACE_NOT_FOUND));

        User user  = userRepository.findByIdAndStatusOrThrow(id, EntityStatus.ACTIVATED );

        if(!isValidPermission(request.getPermission())) {
            throw new ApplicationException(ErrorCode.INVALID_PERMISSION);
        }

        Member member = new Member();
        member.setUser(user);
        member.setPermission(Permission.WORKSPACE_MEMBER);
        member.setWorkspace(workspace);
        return ResponseEntity.ok(memberRepository.save(member));
    }



    public void checkIfUserIsAuthorized(AuthUser authUser) {
        if (authUser.getUserRole() == UserRole.USER) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
    }

    private boolean isValidPermission(Permission permission) {
        for (Permission p : Permission.values()) {
            if (p == permission) {
                return true;
            }
        }
        return false;
    }


}
