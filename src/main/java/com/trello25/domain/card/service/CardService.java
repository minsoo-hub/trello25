package com.trello25.domain.card.service;




import com.trello25.domain.card.dto.request.CreateCardRequest;
import com.trello25.domain.card.dto.request.DeleteCardRequest;
import com.trello25.domain.card.dto.request.UpdateCardRequest;
import com.trello25.domain.card.dto.response.CardDetailResponse;
import com.trello25.domain.card.entity.Card;
import com.trello25.domain.card.repository.CardRepository;
import com.trello25.domain.cardactive.actiontype.ActionType;
import com.trello25.domain.cardactive.dto.CardActiveResponse;
import com.trello25.domain.cardactive.entity.CardActive;
import com.trello25.domain.cardactive.repository.CardActiveRepository;
import com.trello25.domain.comment.dto.CommentResponse;
import com.trello25.domain.common.entity.EntityStatus;
import com.trello25.domain.kanban.entity.Kanban;
import com.trello25.domain.kanban.repository.KanbanRepository;
import com.trello25.domain.manager.entity.Manager;
import com.trello25.domain.member.entity.Member;
import com.trello25.domain.member.entity.Permission;
import com.trello25.domain.member.repository.MemberRepository;
import com.trello25.exception.ApplicationException;
import com.trello25.exception.ErrorCode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final KanbanRepository kanbanRepository;
    private final MemberRepository memberRepository;
    private final CardActiveRepository cardActiveRepository;

    public void createCard(CreateCardRequest createCardRequest) {

        if (createCardRequest.getDeadline().isBefore(LocalDateTime.now())) {
            throw new ApplicationException(ErrorCode.INVALID_DEADLINE);
        }

        Kanban kanban = kanbanRepository.findByIdAndStatus(createCardRequest.getKanbanId(),
                        EntityStatus.ACTIVATED)
                .orElseThrow(() -> new ApplicationException(ErrorCode.KANBAN_NOT_FOUND));

        Member author = memberRepository.findByIdAndStatusOrThrow(createCardRequest.getMemberId(),
                EntityStatus.ACTIVATED);

        if (author.getPermission().equals(Permission.READ_ONLY)) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        Card card = new Card(createCardRequest.getTitle(), createCardRequest.getDescription(),
                createCardRequest.getDeadline(), kanban);
        List<Manager> managerList = memberRepository.findAllByIdIn(createCardRequest.getManagers())
                .stream()
                .map(member -> new Manager(card, member))
                .toList();

        for (Manager manager : managerList) {
            card.addManager(manager);
        }

        cardRepository.save(card);
        CardActive cardActive = new CardActive(card, author, ActionType.CREATE);
        cardActiveRepository.save(cardActive);
        card.addCardActive(cardActive);
    }

    public void updateCard(Long id, UpdateCardRequest updateCardRequest) {

        Card card = cardRepository.findByIdAndStatusOrThrow(id, EntityStatus.ACTIVATED);

        Member author = memberRepository.findByIdAndStatusOrThrow(updateCardRequest.getMemberId(),
                EntityStatus.ACTIVATED);

        if (author.getPermission().equals(Permission.READ_ONLY)) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        if (updateCardRequest.getDeadline().isBefore(LocalDateTime.now())) {
            throw new ApplicationException(ErrorCode.INVALID_DEADLINE);
        }

        if (updateCardRequest.getTitle() != null) {
            card.updateTitle(updateCardRequest.getTitle());
        }

        if (updateCardRequest.getDescription() != null) {
            card.updateDescription(updateCardRequest.getDescription());
        }

        if (updateCardRequest.getDeadline() != null) {
            card.updateDeadline(updateCardRequest.getDeadline());
        }

        if (updateCardRequest.getManagers() != null) {
            List<Manager> managerList = memberRepository.findAllById(updateCardRequest.getManagers())
                    .stream().map(member -> new Manager(card, member)).toList();

            for (Manager manager : managerList) {
                if (card.getManagers().contains(manager)) {
                    throw new ApplicationException(ErrorCode.ALREADY_ASSIGNED_MANAGER);
                }
                card.addManager(manager);
                CardActive cardActive = new CardActive(card, author, ActionType.ASSIGN_MANAGER);
                cardActiveRepository.save(cardActive);
                card.addCardActive(cardActive);
            }
        }

        cardRepository.save(card);
        CardActive cardActive = new CardActive(card, author, ActionType.UPDATE);
        cardActiveRepository.save(cardActive);
        card.addCardActive(cardActive);
    }

    @Transactional(readOnly = true)
    public CardDetailResponse getCard(Long id) {
        if (!cardRepository.existsById(id)) {
            throw new ApplicationException(ErrorCode.CARD_NOT_FOUND);
        }

        Card card = cardRepository.findCardWithCardActiveAndComment(id);

        List<CardActiveResponse> cardActiveResponses = card.getCardActives().stream()
                .map(cardActive -> new CardActiveResponse(cardActive.getId(),
                        cardActive.getMember().getId(), cardActive.getAction()))
                .toList();

        List<CommentResponse> commentResponses = card.getComments().stream()
                .map(comment -> new CommentResponse(comment.getId(), comment.getText()))
                .toList();

        return new CardDetailResponse(card, cardActiveResponses, commentResponses);
    }

    public void deleteCard(Long id, DeleteCardRequest deleteCardRequest) {

        Card card = cardRepository.findByIdAndStatusOrThrow(id, EntityStatus.ACTIVATED);
        Member author = memberRepository.findByIdAndStatus(deleteCardRequest.getMemberId(),EntityStatus.ACTIVATED)
                .orElseThrow(() -> new ApplicationException(ErrorCode.MEMBER_NOT_FOUND));

        if (author.getPermission().equals(Permission.READ_ONLY)) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        card.delete();
        cardRepository.save(card);
        CardActive cardActive = new CardActive(card, author, ActionType.DELETE);
        cardActiveRepository.save(cardActive);
        card.addCardActive(cardActive);
    }
}
