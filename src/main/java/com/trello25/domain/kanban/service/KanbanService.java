package com.trello25.domain.kanban.service;

import static com.trello25.exception.ErrorCode.BOARD_NOT_FOUND;

import com.trello25.domain.board.entity.Board;
import com.trello25.domain.board.repository.BoardRepository;
import com.trello25.domain.common.entity.EntityStatus;
import com.trello25.domain.kanban.AuthUser;
import com.trello25.domain.kanban.dto.request.CreateKanbanRequest;
import com.trello25.domain.kanban.entity.Kanban;
import com.trello25.domain.kanban.repository.KanbanRepository;
import com.trello25.domain.kanbanposition.service.KanbanPositionService;
import com.trello25.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class KanbanService {

    private final BoardRepository boardRepository;
    private final KanbanRepository kanbanRepository;
    private final KanbanPositionService kanbanPositionService;

    public void createKanban(AuthUser authUser, CreateKanbanRequest request) {
        // TODO: 칸반 생성 권한을 가지고 있는 멤버인지 확인 필요, 로그인 기능 구현 완료 시 수정예정

        Board board = boardRepository.findById(request.getBoardId())
                .orElseThrow(() -> new ApplicationException(BOARD_NOT_FOUND));
        if (board.getStatus() == EntityStatus.DELETED) {
            throw new ApplicationException(BOARD_NOT_FOUND);
        }

        Kanban kanban = new Kanban(board, request.getTitle());
        kanbanRepository.save(kanban);
        kanbanPositionService.addKanban(kanban);
    }
}
