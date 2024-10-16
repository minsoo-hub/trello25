package com.trello25.domain.card.entity;


import com.trello25.domain.common.entity.BaseEntity;
import com.trello25.domain.kanban.entity.Kanban;
import jakarta.persistence.Entity;
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
@Getter
@NoArgsConstructor
public class Card extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDateTime deadline;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id", nullable = false)
    private Kanban kanban;

    public Card(String title, String description, LocalDateTime deadline, Kanban kanban) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.kanban = kanban;
    }
}
