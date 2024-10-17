package com.trello25.domain.member.controller;

import com.trello25.domain.auth.dto.AuthUser;
import com.trello25.domain.member.dto.ChangePermissionRequest;
import com.trello25.domain.member.entity.Member;
import com.trello25.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    //member 추가
    @PostMapping("/{workspaceId}/{userId}")
    public ResponseEntity<Member> addMembers(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long workspaceId,
            @PathVariable Long userId
    ){
        memberService.addMembers(authUser,workspaceId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    //member permission 변경
    @PutMapping("/{workspaceId}/{id}")
    public ResponseEntity<Member> changePermission(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long workspaceId,
            @PathVariable Long id,
            @RequestBody ChangePermissionRequest request
    ){
        return memberService.changePermission(authUser,workspaceId, id, request);
    }
}