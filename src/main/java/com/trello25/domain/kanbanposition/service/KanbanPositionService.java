package com.trello25.domain.kanbanposition.service;

import com.trello25.domain.kanban.entity.Kanban;
import com.trello25.domain.kanbanposition.entity.KanbanPosition;
import com.trello25.domain.kanbanposition.repository.KanbanPositionRepository;
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
}
