package com.trello25.domain.board.repository;

import com.trello25.domain.board.entity.Board;
import com.trello25.domain.common.entity.EntityStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByWorkspace_IdAndStatus(@Param("workspaceId") Long id, EntityStatus status);
}
