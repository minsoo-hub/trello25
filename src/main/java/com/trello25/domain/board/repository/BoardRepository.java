package com.trello25.domain.board.repository;

import com.trello25.domain.board.entity.Board;
import com.trello25.domain.common.entity.EntityStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("SELECT b FROM Board b LEFT JOIN FETCH b.workspace w ON w.id = :workspaceId AND b.status = :status ORDER BY b.createdAt DESC")
    List<Board> findByWorkspaceIdAndStatus(@Param("workspaceId") Long id, EntityStatus status);
}
