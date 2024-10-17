package com.trello25.domain.comment.entity;

import com.trello25.domain.card.entity.Card;
import com.trello25.domain.common.entity.BaseEntity;
import com.trello25.domain.common.entity.EntityStatus;
import com.trello25.domain.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String content;

    public Comment(Card card, String content, Member member) {
        this.card = card;
        this.content = content;
        this.member = member;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void delete() {
        this.status = EntityStatus.DELETED;
    }

    public boolean isNotWrittenBy(Member member) {
        return this.member.getId() == member.getId();
    }
}
