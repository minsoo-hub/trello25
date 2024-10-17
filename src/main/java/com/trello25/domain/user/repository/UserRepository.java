package com.trello25.domain.user.repository;

import com.trello25.domain.common.entity.EntityStatus;
import com.trello25.domain.member.entity.Member;
import com.trello25.domain.user.entity.User;
import java.util.List;

import com.trello25.exception.ApplicationException;
import com.trello25.exception.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByIdAndStatus(long id, EntityStatus status);
    List<User> findAllByEmailInAndStatus(List<String> emails, EntityStatus status);
    boolean existsByEmail(String email);

    default User findByIdAndStatusOrThrow(Long id, EntityStatus status) {
        return findByIdAndStatus(id, status).orElseThrow(
                () -> new ApplicationException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
