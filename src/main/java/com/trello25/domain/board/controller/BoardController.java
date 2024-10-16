package com.trello25.domain.board.controller;


import com.trello25.domain.auth.dto.AuthUser;
import com.trello25.domain.board.dto.request.CreateBoardRequest;
import com.trello25.domain.board.dto.request.UpdateBoardRequest;
import com.trello25.domain.board.dto.response.BoardResponse;
import com.trello25.domain.board.service.BoardService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    /**
     * 보드 등록
     * @param id
     * @param createBoardRequest
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/workspaces/{id}/boards")
    public void createBoards(@AuthenticationPrincipal AuthUser authUser, @PathVariable(name = "id") long id, @Valid @RequestBody CreateBoardRequest createBoardRequest){
        boardService.createBoard(authUser.getId(),id, createBoardRequest);
    }

    /**
     * 보드 수정
     * @param id
     * @param updateBoardRequest
     */
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/boards/{id}")
    public void updateBoards(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long id, @Valid @RequestBody UpdateBoardRequest updateBoardRequest){
        boardService.updateBoard(authUser.getId(), id, updateBoardRequest);
    }

    /**
     * 보드 목록 조회
     * @param id
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/workspaces/{id}/boards")
    public List<BoardResponse> getBoardList(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long id){
        List<BoardResponse> responseList = boardService.getBoardList(authUser.getId(), id);
        return responseList;
    }

    /**
     * 보드 단건 조회
     * @param id
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/boards/{id}")
    public BoardResponse getBoard(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long id){
        return boardService.getBoard(authUser.getId(), id);
    }

    /**
     * 보드 삭제
     */
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/boards/{id}")
    public void deleteBoard(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long id){
        boardService.deleteBoard(authUser.getId(), id);
    }

    /**
     * 배경이미지 변경
     * @param authUser
     * @param file
     * @param boardId
     * @throws IOException
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/boards/{boardId}/background")
    public void updateBackgroundImage(@AuthenticationPrincipal AuthUser authUser, @RequestParam("file") MultipartFile file, @PathVariable Long boardId) throws IOException {
        boardService.updateImage(authUser.getId(), boardId, file);
    }

}
