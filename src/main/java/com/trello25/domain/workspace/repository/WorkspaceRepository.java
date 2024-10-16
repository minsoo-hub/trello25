package com.trello25.domain.workspace.repository;

import com.trello25.domain.workspace.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long>, WokrspaceRespositoryCoustom {


}
