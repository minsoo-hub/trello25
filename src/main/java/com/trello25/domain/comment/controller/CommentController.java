package com.trello25.domain.comment.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import com.trello25.domain.auth.dto.AuthUser;
import com.trello25.domain.comment.dto.request.CreateCommentRequest;
import com.trello25.domain.comment.dto.request.UpdateCommentContentRequest;
import com.trello25.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments")
    public ResponseEntity<Void> createComment(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody CreateCommentRequest createCommentRequest
    ) {
        commentService.createComment(authUser, createCommentRequest);
        return ResponseEntity.status(CREATED).build();
    }

    @PatchMapping("/comments/{id}/content")
    public ResponseEntity<Void> updateCommentContent(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable long id,
            @RequestBody UpdateCommentContentRequest request
    ) {
        commentService.updateCommentContent(authUser, id, request);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable long id
    ) {
        commentService.deleteComment(authUser, id);
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
