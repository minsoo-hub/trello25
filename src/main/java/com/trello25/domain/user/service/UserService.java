package com.trello25.domain.user.service;

import com.trello25.domain.common.entity.EntityStatus;
import com.trello25.domain.user.dto.request.ChangePasswordUserRequest;
import com.trello25.domain.user.dto.request.RoleChangeUserRequest;
import com.trello25.domain.user.dto.response.UserResponse;
import com.trello25.domain.user.entity.User;
import com.trello25.domain.user.enums.UserRole;
import com.trello25.domain.user.repository.UserRepository;
import com.trello25.exception.ApplicationException;
import com.trello25.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ID로 유저 조회
    public UserResponse getUser(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        // 상태가 ACTIVATED인 사용자만 조회
        if (user.getStatus() != EntityStatus.ACTIVATED) {
            throw new ApplicationException(ErrorCode.USER_NOT_FOUND);
        }

        return new UserResponse(user.getId(), user.getEmail(), user.getUserRole());
    }

    // 이메일로 유저 조회 (카드에서 사용할 수 있는 기능)
    public UserResponse getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> new UserResponse(user.getId(), user.getEmail(), user.getUserRole()))
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
    }

    // 비밀번호 변경
    @Transactional
    public void changePassword(long userId, ChangePasswordUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new ApplicationException(ErrorCode.SAME_OLD_PASSWORD);
        }

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new ApplicationException(ErrorCode.PASSWORD_MISMATCH);
        }

        user.changePassword(passwordEncoder.encode(request.getNewPassword()));
    }

    // 유저 역할 업데이트
    @Transactional
    public void updateUserRole(long userId, RoleChangeUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        user.updateRole(UserRole.of(request.getRole()));
    }

    public List<UserResponse> getUsersByEmails(List<String> emails) {
        // ACTIVATED 상태의 유저만 조회
        List<User> users = userRepository.findAllByEmailInAndStatus(emails, EntityStatus.ACTIVATED);
        return users.stream()
                .map(user -> new UserResponse(user.getId(), user.getEmail(), user.getUserRole()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteUser(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        // 상태를 DELETED로 변경
        user.setStatus(EntityStatus.DELETED);
    }
}