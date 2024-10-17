package com.trello25.domain.card.repository;


import com.trello25.domain.card.dto.response.SearchCardResponse;
import com.trello25.domain.card.entity.Card;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardQueryRepository {

    Card findCardWithCardActiveAndComment(Long id);

    Page<SearchCardResponse> findCardsByConditions(Long id, String title, String description, LocalDate deadline, Pageable pageable);
}
