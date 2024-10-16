package com.trello25.domain.member.service;

import com.trello25.domain.auth.dto.AuthUser;
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

        checkIfUserIsAuthorized(authUser);
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.WORKSPACE_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

//        // 사용자 ID와 워크스페이스 ID로 멤버 조회
//        Member member = memberRepository.findByUser_IdAndWorkspace_Id(user.getId(), id)
//            .orElseThrow(() -> new ApplicationException(ErrorCode.MEMBER_NOT_FOUND));

        Member member = new Member();
        member.setWorkspace(workspace);
        member.setUser(user);
        member.setPermission(Permission.BOARD_MEMBER);  // Default permission

       return memberRepository.save(member);
    }

    public ResponseEntity<Member> changePermission(AuthUser authUser, Long id, ChangePermissionRequest request) {
        checkIfUserIsAuthorized(authUser);

        Member member = memberRepository.findByIdOrElseThrow(id);

        if(!isValidPermission(request.getPermission())) {
            throw new ApplicationException(ErrorCode.INVALID_PERMISSION);
        }

        member.setPermission(request.getPermission());
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
