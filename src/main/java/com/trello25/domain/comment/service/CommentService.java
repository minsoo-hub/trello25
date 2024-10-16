package com.trello25.domain.comment.service;

import static com.trello25.exception.ErrorCode.CARD_NOT_FOUND;
import static com.trello25.exception.ErrorCode.COMMENT_NOT_FOUND;
import static com.trello25.exception.ErrorCode.UNAUTHORIZED_ACCESS;

import com.trello25.domain.auth.dto.AuthUser;
import com.trello25.domain.card.entity.Card;
import com.trello25.domain.card.repository.CardRepository;
import com.trello25.domain.comment.dto.request.CreateCommentRequest;
import com.trello25.domain.comment.dto.request.UpdateCommentContentRequest;
import com.trello25.domain.comment.entity.Comment;
import com.trello25.domain.comment.repository.CommentRepository;
import com.trello25.domain.common.entity.EntityStatus;
import com.trello25.domain.member.entity.Member;
import com.trello25.domain.member.entity.Permission;
import com.trello25.domain.member.repository.MemberRepository;
import com.trello25.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;
    private final MemberRepository memberRepository;

    public void createComment(AuthUser authUser, CreateCommentRequest request) {
        Member member = memberRepository.findMemberForCommentByCardId(authUser.getId(), request.getCardId())
                .orElseThrow(() -> new ApplicationException(UNAUTHORIZED_ACCESS));
        if (member.getPermission() == Permission.READ_ONLY) {
            throw new ApplicationException(UNAUTHORIZED_ACCESS);
        }

        Card card = cardRepository.findById(request.getCardId())
                .orElseThrow(() -> new ApplicationException(CARD_NOT_FOUND));

        Comment comment = new Comment(card, request.getContent());
        commentRepository.save(comment);
    }

    public void updateCommentContent(AuthUser authUser, long commentId, UpdateCommentContentRequest request) {
        Comment comment = commentRepository.findByIdAndStatus(commentId, EntityStatus.ACTIVATED)
                .orElseThrow(() -> new ApplicationException(COMMENT_NOT_FOUND));

        Member member = memberRepository.findMemberForCommentByCommentId(authUser.getId(), commentId)
                .orElseThrow(() -> new ApplicationException(UNAUTHORIZED_ACCESS));
        if (member.getPermission() == Permission.READ_ONLY) {
            throw new ApplicationException(UNAUTHORIZED_ACCESS);
        }

        comment.updateContent(request.getContent());
    }

    public void deleteComment(AuthUser authUser, long commentId) {
        Comment comment = commentRepository.findByIdAndStatus(commentId, EntityStatus.ACTIVATED)
                .orElseThrow(() -> new ApplicationException(COMMENT_NOT_FOUND));

        Member member = memberRepository.findMemberForCommentByCommentId(authUser.getId(), commentId)
                .orElseThrow(() -> new ApplicationException(UNAUTHORIZED_ACCESS));
        if (member.getPermission() == Permission.READ_ONLY) {
            throw new ApplicationException(UNAUTHORIZED_ACCESS);
        }

        comment.delete();
    }
}
