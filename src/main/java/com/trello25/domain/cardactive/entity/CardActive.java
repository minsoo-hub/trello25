package com.trello25.domain.cardactive.entity;

import com.trello25.domain.card.entity.*;
import com.trello25.domain.cardactive.actiontype.ActionType;
import com.trello25.domain.common.entity.BaseEntity;
import com.trello25.domain.member.entity.Member;
import com.trello25.domain.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class CardActive extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private ActionType action;

    public CardActive(Card card, Member member, ActionType action) {
        this.card = card;
        this.member = member;
        this.action = action;
    }
}
