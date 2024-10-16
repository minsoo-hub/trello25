package com.trello25.domain.auth.service;

import com.trello25.domain.common.entity.EntityStatus;
import com.trello25.domain.user.entity.User;
import com.trello25.domain.user.enums.UserRole;
import com.trello25.domain.user.repository.UserRepository;
import com.trello25.exception.ApplicationException;
import com.trello25.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.trello25.config.JwtUtil;
import com.trello25.domain.auth.dto.response.SigninResponse;
import com.trello25.domain.auth.dto.request.SigninRequest;
import com.trello25.domain.auth.dto.response.SignupResponse;
import com.trello25.domain.auth.dto.request.SignupRequest;
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public SignupResponse signup(SignupRequest signupRequest) {
        // 이메일 중복 확인 (상태와 상관없이)
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new ApplicationException(ErrorCode.EMAIL_DUPLICATED);
        }

        // 비밀번호 암호화 및 유저 생성
        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());
        User newUser = new User(
                signupRequest.getEmail(),
                encodedPassword,
                UserRole.of(signupRequest.getUserRole())
        );
        newUser.setStatus(EntityStatus.ACTIVATED);  // 기본 상태는 ACTIVATED

        User savedUser = userRepository.save(newUser);

        // JWT 토큰 생성
        String bearerToken = jwtUtil.createToken(savedUser.getId(), savedUser.getEmail(), savedUser.getUserRole());

        return new SignupResponse(bearerToken);
    }

    public SigninResponse signin(SigninRequest signinRequest) {
        User user = userRepository.findByEmail(signinRequest.getEmail())
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        // 상태가 ACTIVATED인지 확인
        if (user.getStatus() != EntityStatus.ACTIVATED) {
            throw new ApplicationException(ErrorCode.USER_NOT_FOUND);
        }

        // 비밀번호 검증
        if (!passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())) {
            throw new ApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        // JWT 토큰 생성
        String bearerToken = jwtUtil.createToken(user.getId(), user.getEmail(), user.getUserRole());

        return new SigninResponse(bearerToken);
    }
}