package com.trello25.domain.card.repository;

import com.trello25.domain.card.entity.Card;
import com.trello25.domain.common.entity.EntityStatus;
import com.trello25.exception.ApplicationException;
import com.trello25.exception.ErrorCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {

}
