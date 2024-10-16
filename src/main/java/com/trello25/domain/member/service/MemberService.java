package com.trello25.domain.member.service;

import com.trello25.domain.member.entity.Member;
import com.trello25.domain.member.repository.MemberRepository;
import com.trello25.domain.user.entity.User;
import com.trello25.domain.user.repository.UserRepository;
import com.trello25.exception.ApplicationException;
import com.trello25.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final UserRepository userRepository;
    private final MemberRepository memberRepository;

    // 로그인된 사용자의 이메일을 기반으로 멤버 ID 가져오기
    public Long getLoggedInMemberId(Long id) {
        // SecurityContextHolder에서 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        // principal에서 이메일 가져오기
        String email = (String) authentication.getPrincipal();

        // 이메일을 통해 사용자 정보 조회
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        // 사용자 ID와 워크스페이스 ID로 멤버 조회
        Member member = memberRepository.findByUserIdAndWorkspaceId(user.getId(), id)
            .orElseThrow(() -> new ApplicationException(ErrorCode.MEMBER_NOT_FOUND));

        return member.getId();  // 멤버 ID 반환
    }
}
