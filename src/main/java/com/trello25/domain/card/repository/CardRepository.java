package com.trello25.domain.card.repository;

import com.trello25.domain.card.entity.Card;
import com.trello25.domain.kanban.entity.Kanban;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findAllByKanban(final Kanban kanban);
}