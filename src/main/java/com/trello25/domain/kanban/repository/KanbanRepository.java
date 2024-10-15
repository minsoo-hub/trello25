package com.trello25.domain.kanban.repository;

import com.trello25.domain.common.entity.EntityStatus;
import com.trello25.domain.kanban.entity.Kanban;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KanbanRepository extends JpaRepository<Kanban, Long> {

    Optional<Kanban> findByIdAndStatus(Long id, EntityStatus status);
}
