package com.trello25.domain.card.repository;

import javax.smartcardio.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {

}
