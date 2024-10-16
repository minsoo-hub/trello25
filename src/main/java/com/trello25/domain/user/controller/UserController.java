package com.trello25.domain.user.controller;

import com.trello25.domain.auth.dto.AuthUser;
import com.trello25.domain.user.dto.request.ChangePasswordUserRequest;
import com.trello25.domain.user.dto.request.RoleChangeUserRequest;
import com.trello25.domain.user.dto.response.UserResponse;
import com.trello25.domain.user.enums.UserRole;
import com.trello25.domain.user.service.UserService;
import com.trello25.exception.ApplicationException;
import com.trello25.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 인증된 사용자의 정보를 가져오기 위해 @AuthenticationPrincipal 사용
    @GetMapping("/users/me")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal AuthUser authUser) {
        // @AuthenticationPrincipal을 통해 인증된 사용자 정보 (AuthUser) 주입
        return ResponseEntity.ok(userService.getUser(authUser.getId()));
    }

    // 비밀번호 변경
    @PutMapping("/users/{id}/password")
    public ResponseEntity<Void> changePassword(@AuthenticationPrincipal AuthUser authUser,
                                               @PathVariable long id,
                                               @RequestBody ChangePasswordUserRequest changePasswordUserRequest) {
        // 본인 확인을 위해 @AuthenticationPrincipal 사용
        if (authUser.getId() != id) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
        userService.changePassword(id, changePasswordUserRequest);
        return ResponseEntity.noContent().build();
    }

    // 역할 변경
    @PutMapping("/users/{id}/role")
    public ResponseEntity<Void> updateRole(@AuthenticationPrincipal AuthUser authUser,
                                           @PathVariable long id,
                                           @RequestBody RoleChangeUserRequest userRoleChangeRequest) {
        // 관리자 권한이 있어야만 역할 변경 가능
        if (!authUser.getUserRole().equals(UserRole.ADMIN)) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
        userService.updateUserRole(id, userRoleChangeRequest);
        return ResponseEntity.noContent().build();
    }

    //유저 검색
    @PostMapping("/by-emails")
    public ResponseEntity<List<UserResponse>> getUsersByEmails(@RequestBody List<String> emails) {
        List<UserResponse> users = userService.getUsersByEmails(emails);
        return ResponseEntity.ok(users);
    }

    // 유저 삭제
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal AuthUser authUser, @PathVariable long id) {
        // 본인 확인 또는 관리자 권한 확인
        if (!authUser.getId().equals(id) && !authUser.getUserRole().equals(UserRole.ADMIN)) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

