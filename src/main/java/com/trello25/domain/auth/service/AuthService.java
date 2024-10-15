package com.trello25.domain.auth.service;

import com.trello25.domain.user.entity.User;
import com.trello25.domain.user.enums.UserRole;
import com.trello25.domain.user.repository.UserRepository;
import com.trello25.exception.ApplicationException;
import com.trello25.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.trello25.config.JwtUtil;
import com.trello25.config.PasswordEncoder;
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
        // 이메일 중복 확인
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new ApplicationException(ErrorCode.EMAIL_DUPLICATED);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        // 역할 설정
        UserRole userRole = UserRole.of(signupRequest.getUserRole());

        // 새로운 유저 객체 생성
        User newUser = new User(signupRequest.getEmail(), encodedPassword, userRole);

        // 유저 저장
        User savedUser = userRepository.save(newUser);

        // JWT 토큰 생성
        String bearerToken = jwtUtil.createToken(savedUser.getId(), savedUser.getEmail(), userRole);

        // 응답 반환
        return new SignupResponse(bearerToken);
    }

    public SigninResponse signin(SigninRequest signinRequest) {
        // 이메일로 유저 조회
        User user = userRepository.findByEmail(signinRequest.getEmail())
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        // 비밀번호 검증
        if (!passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())) {
            throw new ApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        // JWT 토큰 생성
        String bearerToken = jwtUtil.createToken(user.getId(), user.getEmail(), user.getUserRole());

        // 응답 반환
        return new SigninResponse(bearerToken);
    }
}