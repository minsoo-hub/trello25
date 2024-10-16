package com.trello25.domain.member.repository;

import com.trello25.domain.member.entity.Member;
import com.trello25.exception.ApplicationException;
import com.trello25.exception.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import com.trello25.domain.member.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    default Member findByIdOrElseThrow(final long id) {
        return findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Query("SELECT m FROM Member m WHERE m.workspace.id = :workspaceId")
    List<Member> findByWorkspaceId(Long id);

    Optional<Member> findByUserIdAndWorkspaceId(Long userId, Long workspaceId);
}

