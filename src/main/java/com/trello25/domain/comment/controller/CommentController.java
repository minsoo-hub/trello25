package com.trello25.domain.comment.controller;

import static org.springframework.http.HttpStatus.CREATED;

import com.trello25.domain.auth.dto.AuthUser;
import com.trello25.domain.comment.dto.request.CreateCommentRequest;
import com.trello25.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
}
