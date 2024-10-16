package com.trello25.domain.kanban.service;

import static com.trello25.exception.ErrorCode.BOARD_NOT_FOUND;
import static com.trello25.exception.ErrorCode.KANBAN_NOT_FOUND;

import com.trello25.domain.auth.dto.AuthUser;
import com.trello25.domain.board.entity.Board;
import com.trello25.domain.board.repository.BoardRepository;
import com.trello25.domain.card.dto.response.CardResponse;
import com.trello25.domain.card.repository.CardRepository;
import com.trello25.domain.common.entity.EntityStatus;
import com.trello25.domain.kanban.dto.request.CreateKanbanRequest;
import com.trello25.domain.kanban.dto.request.UpdateKanbanPositionRequest;
import com.trello25.domain.kanban.dto.request.UpdateKanbanTitleRequest;
import com.trello25.domain.kanban.dto.response.KanbanResponse;
import com.trello25.domain.kanban.entity.Kanban;
import com.trello25.domain.kanban.repository.KanbanRepository;
import com.trello25.domain.kanbanposition.entity.KanbanPosition;
import com.trello25.domain.kanbanposition.service.KanbanPositionService;
import com.trello25.exception.ApplicationException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final CardRepository cardRepository;

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

    public void deleteKanban(AuthUser authUser, long id) {
        // TODO: 칸반 삭제 권한을 가지고 있는 멤버인지 확인 필요, 로그인 기능 구현 완료 시 수정예정

        Kanban kanban = kanbanRepository.findByIdAndStatus(id, EntityStatus.ACTIVATED)
            .orElseThrow(() -> new ApplicationException(KANBAN_NOT_FOUND));
        kanban.delete();
        kanbanPositionService.deleteKanban(kanban);
    }

    public void updateKanbanTitle(AuthUser authUser, long id, UpdateKanbanTitleRequest request) {
        // TODO: 칸반 수정 권한을 가지고 있는 멤버인지 확인 필요, 로그인 기능 구현 완료 시 수정예정

        Kanban kanban = kanbanRepository.findByIdAndStatus(id, EntityStatus.ACTIVATED)
            .orElseThrow(() -> new ApplicationException(KANBAN_NOT_FOUND));
        kanban.updateTitle(request.getTitle());
    }

    public void updateKanbanPosition(AuthUser authUser, long id, UpdateKanbanPositionRequest request) {
        // TODO: 칸반 수정 권한을 가지고 있는 멤버인지 확인 필요, 로그인 기능 구현 완료 시 수정예정

        Kanban kanban = kanbanRepository.findByIdAndStatus(id, EntityStatus.ACTIVATED)
            .orElseThrow(() -> new ApplicationException(KANBAN_NOT_FOUND));
        kanbanPositionService.updateKanbanPosition(kanban, request.getPosition());
    }

    @Transactional(readOnly = true)
    public List<KanbanResponse> getKanbans(long boardId) {
        List<Kanban> kanbans = kanbanRepository.findAllByBoardIdAndStatus(boardId, EntityStatus.ACTIVATED);
        KanbanPosition kanbanPosition = kanbanPositionService.getKanbanPosition(boardId);

        List<Long> positions = kanbanPosition.getPositions();
        Map<Long, Integer> positionMap = new HashMap<>();
        for (int position = 0; position < positions.size(); position++) {
            Long kanbanId = positions.get(position);
            positionMap.put(kanbanId, position);
        }

        return kanbans.stream()
            .sorted(Comparator.comparingInt(o -> positionMap.get(o.getId())))
            .map(kanban -> {
                List<CardResponse> cardResponses = cardRepository.findAllByKanban(kanban).stream()
                    .map(CardResponse::new)
                    .toList();
                return new KanbanResponse(kanban, cardResponses);
            })
            .toList();
    }
}