package com.trello25.domain.board.entity;

import com.trello25.domain.board.enums.BackColors;
import com.trello25.domain.common.entity.BaseEntity;
import com.trello25.domain.common.entity.EntityStatus;
import com.trello25.domain.kanban.entity.Kanban;
import com.trello25.domain.workspace.entity.Workspace;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Enumerated( EnumType.STRING)
    private BackColors backColor;

    @Column
    private String originImageName;

    @Column
    private String imagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Kanban> kanbanList = new ArrayList<>();

    public Board(String title, BackColors backColor, String imagePath, Workspace workspace) {
        this.title = title;
        this.imagePath = imagePath;
        this.backColor = backColor;
        this.workspace = workspace;
    }

    public void updateBoard(String title, BackColors backColor) {
        this.title = title;
        this.backColor = backColor;
    }

    public void delete(){
        this.setStatus(EntityStatus.DELETED);
        kanbanList.forEach(Kanban::delete);
    }

    public void updateBackground(String originImageName, String imagePath) {
        this.originImageName = originImageName;
        this.imagePath = imagePath;
    }
}
