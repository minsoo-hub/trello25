package com.trello25.domain.kanbanposition.service;

import static com.trello25.exception.ErrorCode.KANBAN_POSITION_NOT_FOUND;

import com.trello25.domain.kanban.entity.Kanban;
import com.trello25.domain.kanbanposition.entity.KanbanPosition;
import com.trello25.domain.kanbanposition.repository.KanbanPositionRepository;
import com.trello25.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class KanbanPositionService {

    private final KanbanPositionRepository kanbanPositionRepository;

    public void addKanban(Kanban kanban) {
        KanbanPosition kanbanPosition = kanbanPositionRepository.findByBoard(kanban.getBoard())
                .orElse(new KanbanPosition(kanban));

        if (kanbanPosition.getId() != null) {
            kanbanPosition.addKanban(kanban.getId());
        }

        kanbanPositionRepository.save(kanbanPosition);
    }

    public void deleteKanban(Kanban kanban) {
        KanbanPosition kanbanPosition = kanbanPositionRepository.findByBoard(kanban.getBoard())
                .orElseThrow(() -> new ApplicationException(KANBAN_POSITION_NOT_FOUND));
        kanbanPosition.deleteKanban(kanban.getId());
    }

    public void updateKanbanPosition(Kanban kanban, int position) {
        KanbanPosition kanbanPosition = kanbanPositionRepository.findByBoard(kanban.getBoard())
                .orElseThrow(() -> new ApplicationException(KANBAN_POSITION_NOT_FOUND));
        kanbanPosition.updateKanbanPosition(kanban.getId(), position);
    }

    public KanbanPosition getKanbanPosition(long boardId) {
        return kanbanPositionRepository.findByBoardId(boardId)
                .orElseThrow(() -> new ApplicationException(KANBAN_POSITION_NOT_FOUND));
    }
}
