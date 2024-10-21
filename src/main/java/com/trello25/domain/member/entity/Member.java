package com.trello25.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trello25.domain.common.entity.BaseEntity;
import com.trello25.domain.user.entity.User;
import com.trello25.domain.workspace.entity.Workspace;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "member")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Permission permission;

    //@JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

   public Member(Permission permission, User user, Workspace workspace){
        this.permission = permission;
        this.user =user;
        this.workspace=workspace;
    }
}