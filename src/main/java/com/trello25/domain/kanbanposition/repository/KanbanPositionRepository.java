package com.trello25.domain.kanbanposition.repository;

import com.trello25.domain.board.entity.Board;
import com.trello25.domain.kanbanposition.entity.KanbanPosition;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KanbanPositionRepository extends JpaRepository<KanbanPosition, Long> {

    Optional<KanbanPosition> findByBoard(final Board board);

    Optional<KanbanPosition> findByBoardId(final long boardId);
}
