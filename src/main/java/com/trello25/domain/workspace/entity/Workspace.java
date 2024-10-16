package com.trello25.domain.workspace.entity;

import com.trello25.domain.board.entity.Board;
import com.trello25.domain.common.entity.BaseEntity;
import com.trello25.domain.common.entity.EntityStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name="worksapce")
public class Workspace extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @OneToMany(mappedBy = "workspace", fetch = FetchType.EAGER)
    private List<Board> boards = new ArrayList<>();

    public Workspace (
            String title,
            String description
    ){
        this.title = title;
        this.description = description;
    }

    public void update(String title, String description) {
        this.title = title;
        this.description = description;

    }

    public void delete(){
        this.setStatus(EntityStatus.DELETED);
    }
}
