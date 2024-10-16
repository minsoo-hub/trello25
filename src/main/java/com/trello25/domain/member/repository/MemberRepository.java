package com.trello25.domain.member.repository;

import com.trello25.domain.member.entity.Member;
import com.trello25.exception.ApplicationException;
import com.trello25.exception.ErrorCode;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    default Member findByIdOrElseThrow(final long id) {
        return findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.MEMBER_NOT_FOUND));
    }
    List<Member> findByWorkspace_Id(Long id);

    Optional<Member> findByUser_IdAndWorkspace_Id(Long userId, Long workspaceId);
}
