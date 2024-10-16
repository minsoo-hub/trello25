package com.trello25.domain.comment.repository;

import com.trello25.domain.comment.entity.Comment;
import com.trello25.domain.common.entity.EntityStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByIdAndStatus(Long id, EntityStatus status);
}
