package com.trello25.domain.card.repository;


import com.trello25.domain.card.entity.Card;

public interface CardQueryRepository {

    Card findCardWithCardActiveAndComment(Long id);
}
