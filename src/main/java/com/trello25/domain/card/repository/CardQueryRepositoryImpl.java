package com.trello25.domain.card.repository;

import static com.trello25.domain.card.entity.QCard.card;
import static com.trello25.domain.cardactive.entity.QCardActive.cardActive;
import static com.trello25.domain.comment.entity.QComment.comment;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.trello25.domain.card.dto.response.SearchCardResponse;
import com.trello25.domain.card.entity.Card;
import com.trello25.domain.common.entity.EntityStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    @Override
    public Page<SearchCardResponse> findCardsByConditions(Long id, String title, String description,
            LocalDate deadline, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(card.kanban.board.id.eq(id));

        if (title != null && !title.isEmpty()) {
            builder.and(card.title.containsIgnoreCase(title));
        }
        if (description != null && !description.isEmpty()) {
            builder.and(card.description.containsIgnoreCase(description));
        }
        if (deadline != null) {
            builder.and(card.deadline.eq(deadline));
        }

        List<Card> cards = queryFactory
                .selectFrom(card)
                .distinct()
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(card)
                .where(builder)
                .fetchCount();

        List<SearchCardResponse> cardResponses = cards.stream()
                .map(card -> new SearchCardResponse(
                        card.getId(),
                        card.getTitle(),
                        card.getDescription(),
                        card.getDeadline()))
                .collect(Collectors.toList());

        return new PageImpl<>(cardResponses, pageable, total);
    }
}
