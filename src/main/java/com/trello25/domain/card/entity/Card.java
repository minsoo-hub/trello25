package com.trello25.domain.card.entity;


import com.trello25.domain.cardactive.entity.CardActive;
import com.trello25.domain.comment.entity.Comment;
import com.trello25.domain.common.entity.BaseEntity;
import com.trello25.domain.common.entity.EntityStatus;
import com.trello25.domain.kanban.entity.Kanban;
import com.trello25.domain.manager.entity.Manager;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;


@Entity
@Getter
@NoArgsConstructor
@Table(name = "cards")
public class Card extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDate deadline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kanban_id", nullable = false)
    private Kanban kanban;

    @OneToMany(mappedBy = "card")
    private List<Manager> managers = new ArrayList<>();

    @OneToMany(mappedBy = "card")
    @BatchSize(size = 10)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "card")
    private List<CardActive> cardActives = new ArrayList<>();

    public Card(String title, String description, LocalDate deadline, Kanban kanban
    ) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.kanban = kanban;
    }

    public void addManager(Manager manager) {
        managers.add(manager);
    }

    public void addCardActive(CardActive cardActive) {
        cardActives.add(cardActive);
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public void delete() {
        this.setStatus(EntityStatus.DELETED);
    }
}
