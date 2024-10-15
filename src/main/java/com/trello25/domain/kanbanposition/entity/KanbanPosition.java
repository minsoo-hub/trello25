package com.trello25.domain.kanbanposition.entity;

import static com.trello25.exception.ErrorCode.INVALID_KANBAN_POSITION;

import com.trello25.domain.board.entity.Board;
import com.trello25.domain.kanban.entity.Kanban;
import com.trello25.exception.ApplicationException;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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

    public void deleteKanban(long kanbanId) {
        String kanbanIdString = String.valueOf(kanbanId);
        this.positions = Arrays.stream(this.positions.split(","))
                .filter(position -> !position.equals(kanbanIdString))
                .collect(Collectors.joining(","));
    }

    public void updateKanbanPosition(long kanbanId, int position) {
        String kanbanIdString = String.valueOf(kanbanId);
        List<String> positions = Arrays.stream(this.positions.split(","))
                .filter(p -> !p.equals(kanbanIdString))
                .collect(Collectors.toList());

        if (position < 0 || positions.size() < position) {
            throw new ApplicationException(INVALID_KANBAN_POSITION);
        }

        positions.add(position, String.valueOf(kanbanId));
        this.positions = String.join(",", positions);
    }

    public List<Long> getPositions() {
        if (positions.isBlank()) {
            return new ArrayList<>();
        }
        return Arrays.stream(this.positions.split(","))
                .map(Long::valueOf)
                .toList();
    }
}
