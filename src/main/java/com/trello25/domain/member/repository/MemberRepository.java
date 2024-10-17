package com.trello25.domain.member.repository;

import com.trello25.domain.common.entity.EntityStatus;
import com.trello25.domain.member.entity.Member;
import com.trello25.exception.ApplicationException;
import com.trello25.exception.ErrorCode;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {

    default Member findByIdAndStatusOrThrow(Long id, EntityStatus status) {
        return findByIdAndStatus(id, status).orElseThrow(
                () -> new ApplicationException(ErrorCode.MEMBER_NOT_FOUND));
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

    @Query("select m from Member m "
            + "join m.workspace w "
            + "join Board b on b.workspace.id = w.id "
            + "join Kanban k on k.board.id = b.id "
            + "join Card c on c.kanban.id = k.id "
            + "where m.user.id = :userId and c.id = :cardId")
    Optional<Member> findMemberForCommentByCardId(long userId, long cardId);

    @Query("select m from Member m "
            + "join m.workspace w "
            + "join Board b on b.workspace.id = w.id "
            + "join Kanban k on k.board.id = b.id "
            + "join Card ca on ca.kanban.id = k.id "
            + "join Comment co on co.card.id = ca.id "
            + "where m.user.id = :userId and co.id = :commentId")
    Optional<Member> findMemberForCommentByCommentId(long userId, long commentId);

    Optional<Member> findByIdAndStatus(Long id, EntityStatus status);
    List<Member> findAllByIdIn(List<Long> ids);
}
