package com.trello25.domain.user.service;

import com.trello25.config.PasswordEncoder;
import com.trello25.domain.user.dto.request.ChangePasswordUserRequest;
import com.trello25.domain.user.dto.request.RoleChangeUserRequest;
import com.trello25.domain.user.dto.response.UserResponse;
import com.trello25.domain.user.entity.User;
import com.trello25.domain.user.enums.UserRole;
import com.trello25.domain.user.repository.UserRepository;
import com.trello25.exception.ApplicationException;
import com.trello25.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        return new UserResponse(user.getId(), user.getEmail(), user.getUserRole());
    }

    // 이메일로 유저 조회 (카드에서 사용할 수 있는 기능)
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        return new UserResponse(user.getId(), user.getEmail(), user.getUserRole());
    }

    // 비밀번호 변경
    @Transactional
    public void changePassword(long userId, ChangePasswordUserRequest userChangePasswordRequest) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        // 새로운 비밀번호가 기존 비밀번호와 동일하면 예외 발생
        if (passwordEncoder.matches(userChangePasswordRequest.getNewPassword(), user.getPassword())) {
            throw new ApplicationException(ErrorCode.SAME_OLD_PASSWORD);
        }

        // 기존 비밀번호가 일치하지 않으면 예외 발생
        if (!passwordEncoder.matches(userChangePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new ApplicationException(ErrorCode.PASSWORD_MISMATCH);
        }

        // 비밀번호 변경
        user.changePassword(passwordEncoder.encode(userChangePasswordRequest.getNewPassword()));
    }

    @Transactional
    public void updateUserRole(long userId, RoleChangeUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        // 역할 업데이트 (문자열을 UserRole enum으로 변환)
        user.updateRole(UserRole.of(request.getRole()));  // UserRole.of() 메서드로 변환
    }
}