package com.trello25.domain.workspace.repository;

import com.trello25.domain.workspace.entity.Workspace;

import java.util.List;

public interface WorkspaceRepositoryCustom {
    List<Workspace> findAllSpace();
}
