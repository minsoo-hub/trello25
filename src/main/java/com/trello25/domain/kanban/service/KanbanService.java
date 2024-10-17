package com.trello25.domain.kanban.service;

import static com.trello25.exception.ErrorCode.*;
import static com.trello25.exception.ErrorCode.BOARD_NOT_FOUND;
import static com.trello25.exception.ErrorCode.KANBAN_NOT_FOUND;
import static com.trello25.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.trello25.exception.ErrorCode.UNAUTHORIZED_ACCESS;

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
import com.trello25.domain.member.entity.Member;
import com.trello25.domain.member.entity.Permission;
import com.trello25.domain.member.repository.MemberRepository;
import com.trello25.exception.ApplicationException;

import java.util.*;

import com.trello25.exception.ErrorCode;
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
    private final MemberRepository memberRepository;

    public void createKanban(AuthUser authUser, CreateKanbanRequest request) {
        Member member = memberRepository.findMemberForKanbanByBoardId(authUser.getId(), request.getBoardId())
                .orElseThrow(() -> new ApplicationException(MEMBER_NOT_FOUND));
        if (member.getPermission() == Permission.READ_ONLY) {
            throw new ApplicationException(UNAUTHORIZED_ACCESS);
        }

        Board board = boardRepository.findById(request.getBoardId())
                .orElseThrow(() -> new ApplicationException(BOARD_NOT_FOUND));
        if (board.getStatus() == EntityStatus.DELETED) {
            throw new ApplicationException(BOARD_NOT_FOUND);
        }

        Kanban kanban = new Kanban(board, request.getTitle());
        kanbanRepository.save(kanban);
        kanbanPositionService.addKanban(kanban);
    }

    public void deleteKanban(AuthUser authUser, long kanbanId) {
        Kanban kanban = kanbanRepository.findByIdAndStatus(kanbanId, EntityStatus.ACTIVATED)
                .orElseThrow(() -> new ApplicationException(KANBAN_NOT_FOUND));

        Member member = memberRepository.findMemberForKanbanByKanbanId(authUser.getId(), kanbanId)
                .orElseThrow(() -> new ApplicationException(MEMBER_NOT_FOUND));
        if (member.getPermission() == Permission.READ_ONLY) {
            throw new ApplicationException(UNAUTHORIZED_ACCESS);
        }

        kanban.delete();
        kanbanPositionService.deleteKanban(kanban);
    }

    public void updateKanbanTitle(AuthUser authUser, long kanbanId, UpdateKanbanTitleRequest request) {
        Kanban kanban = kanbanRepository.findByIdAndStatus(kanbanId, EntityStatus.ACTIVATED)
                .orElseThrow(() -> new ApplicationException(KANBAN_NOT_FOUND));

        Member member = memberRepository.findMemberForKanbanByKanbanId(authUser.getId(), kanbanId)
                .orElseThrow(() -> new ApplicationException(MEMBER_NOT_FOUND));
        if (member.getPermission() == Permission.READ_ONLY) {
            throw new ApplicationException(UNAUTHORIZED_ACCESS);
        }

        kanban.updateTitle(request.getTitle());
    }

    public void updateKanbanPosition(AuthUser authUser, long kanbanId, UpdateKanbanPositionRequest request) {
        Kanban kanban = kanbanRepository.findByIdAndStatus(kanbanId, EntityStatus.ACTIVATED)
                .orElseThrow(() -> new ApplicationException(KANBAN_NOT_FOUND));

        Member member = memberRepository.findMemberForKanbanByKanbanId(authUser.getId(), kanbanId)
                .orElseThrow(() -> new ApplicationException(MEMBER_NOT_FOUND));
        if (member.getPermission() == Permission.READ_ONLY) {
            throw new ApplicationException(UNAUTHORIZED_ACCESS);
        }

        kanbanPositionService.updateKanbanPosition(kanban, request.getPosition());
    }

    @Transactional(readOnly = true)
    public List<KanbanResponse> getKanbans(long boardId) {
        List<Kanban> kanbans = kanbanRepository.findAllByBoardIdAndStatus(boardId, EntityStatus.ACTIVATED);
        KanbanPosition kanbanPosition = null;
        try {
            kanbanPosition = kanbanPositionService.getKanbanPosition(boardId);
        } catch (ApplicationException e) {
            if (e.getMessage().equals(KANBAN_POSITION_NOT_FOUND.getMessage())) {
                return new ArrayList<>();
            }
        }

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