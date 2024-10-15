package com.trello25.domain.kanbanposition.entity;

import com.trello25.domain.board.entity.Board;
import com.trello25.domain.kanban.entity.Kanban;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KanbanPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private String positions;

    public KanbanPosition(Kanban kanban) {
        this.board = kanban.getBoard();
        this.positions = String.valueOf(kanban.getId());
    }

    public void addKanban(long kanbanId) {
        if (positions.isBlank()) {
            this.positions = String.valueOf(kanbanId);
            return;
        }

        StringBuilder sb = new StringBuilder(this.positions)
                .append(",")
                .append(kanbanId);
        this.positions = sb.toString();
    }
}
