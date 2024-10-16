package com.trello25.domain.card.repository;

import static com.trello25.domain.card.entity.QCard.card;
import static com.trello25.domain.cardactive.entity.QCardActive.cardActive;
import static com.trello25.domain.comment.entity.QComment.comment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.trello25.domain.card.entity.Card;
import com.trello25.domain.common.entity.EntityStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CardQueryRepositoryImpl implements CardQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Card findCardWithCardActiveAndComment(Long id) {
        return queryFactory
                .selectFrom(card)
                .distinct()
                .leftJoin(card.comments, comment)
                .leftJoin(card.cardActives, cardActive).fetchJoin()
                .where(card.status.eq(EntityStatus.ACTIVATED).and(card.id.eq(id)))
                .fetchOne();
    }
}
