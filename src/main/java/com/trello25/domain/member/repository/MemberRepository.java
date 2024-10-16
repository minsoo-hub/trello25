package com.trello25.domain.member.repository;

import com.trello25.domain.member.entity.Member;
import com.trello25.exception.ApplicationException;
import com.trello25.exception.ErrorCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    default Member findByIdOrElseThrow(final long id) {
        return findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Query("select m from Member m "
            + "join m.workspace w "
            + "join Board b on b.workspace.id = w.id "
            + "where m.user.id = :userId and b.id = :boardId")
    Optional<Member> findMemberForKanbanByBoardId(long userId, long boardId);

    @Query("select m from Member m "
            + "join m.workspace w "
            + "join Board b on b.workspace.id = w.id "
            + "join Kanban k on k.board.id = b.id "
            + "where m.user.id = :userId and k.id = :kanbanId")
    Optional<Member> findMemberForKanbanByKanbanId(long userId, long kanbanId);
}
