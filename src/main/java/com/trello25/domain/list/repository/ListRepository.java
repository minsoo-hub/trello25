package com.trello25.domain.list.repository;

import com.trello25.domain.list.entity.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListRepository extends JpaRepository<List, Long> {
}
