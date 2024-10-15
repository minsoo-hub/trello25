package com.trello25.domain.user.controller;

import com.trello25.domain.user.dto.request.ChangePasswordUserRequest;
import com.trello25.domain.user.dto.request.RoleChangeUserRequest;
import com.trello25.domain.user.dto.response.UserResponse;
import com.trello25.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }


    @PutMapping("/users/{userId}/password")
    public ResponseEntity<Void> changePassword(@PathVariable long userId,
                                               @RequestBody ChangePasswordUserRequest changePasswordUserRequest) {
        userService.changePassword(userId, changePasswordUserRequest);
        return ResponseEntity.noContent().build(); // 비밀번호 변경 후 성공 응답
    }


    @PutMapping("/users/{userId}/role")
    public ResponseEntity<Void> updateRole(@PathVariable long userId,
                                           @RequestBody RoleChangeUserRequest userRoleChangeRequest) {
        userService.updateUserRole(userId, userRoleChangeRequest);
        return ResponseEntity.noContent().build(); // 역할 변경 후 성공 응답
    }
}