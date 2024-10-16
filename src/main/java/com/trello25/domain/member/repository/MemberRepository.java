package com.trello25.domain.member.repository;

import com.trello25.domain.member.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository {

    @Query("SELECT m FROM Member m WHERE m.workspace.id = :workspaceId")
    List<Member> findByWorkspaceId(Long id);

    Optional<Member> findByUserIdAndWorkspaceId(Long userId, Long workspaceId);
}
