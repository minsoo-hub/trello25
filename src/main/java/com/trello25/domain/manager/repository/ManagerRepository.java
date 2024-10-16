package com.trello25.domain.manager.repository;

import com.trello25.domain.manager.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, Long> {

}
