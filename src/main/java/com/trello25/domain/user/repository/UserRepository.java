package com.trello25.domain.user.repository;

import com.trello25.domain.common.entity.EntityStatus;
import com.trello25.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findAllByEmailInAndStatus(List<String> emails, EntityStatus status);
    boolean existsByEmail(String email);
}
