package com.trello25.domain.kanban.repository;

import com.trello25.domain.kanban.entity.Kanban;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KanbanRepository extends JpaRepository<Kanban, Long> {
}
