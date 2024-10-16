package com.trello25.domain.member.repository;

import com.trello25.domain.common.entity.EntityStatus;
import com.trello25.domain.member.entity.Member;
import com.trello25.exception.ApplicationException;
import com.trello25.exception.ErrorCode;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    default Member findByIdAndStatusOrThrow(Long id, EntityStatus status) {
        return findByIdAndStatus(id, status).orElseThrow(
                () -> new ApplicationException(ErrorCode.MEMBER_NOT_FOUND));
    }

    Optional<Member> findByIdAndStatus(Long id, EntityStatus status);
    List<Member> findAllByIdIn(List<Long> ids);

}
