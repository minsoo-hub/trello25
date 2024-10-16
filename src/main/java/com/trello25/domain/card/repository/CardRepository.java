package com.trello25.domain.card.repository;

import com.trello25.domain.card.entity.Card;
import com.trello25.domain.common.entity.EntityStatus;
import com.trello25.domain.kanban.entity.Kanban;
import com.trello25.exception.ApplicationException;
import com.trello25.exception.ErrorCode;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long>, CardQueryRepository {
    default Card findByIdAndStatusOrThrow(Long id,EntityStatus status) {
        return findByIdAndStatus(id, status).orElseThrow(() -> new ApplicationException(ErrorCode.CARD_NOT_FOUND));
    }

    List<Card> findAllByKanban(final Kanban kanban);
    Optional<Card> findByIdAndStatus(Long id, EntityStatus status);
}
