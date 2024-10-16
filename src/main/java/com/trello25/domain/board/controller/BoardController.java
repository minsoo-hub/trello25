package com.trello25.domain.board.controller;


import com.trello25.domain.board.dto.request.CreateBoardRequest;
import com.trello25.domain.board.dto.request.UpdateBoardRequest;
import com.trello25.domain.board.dto.response.BoardResponse;
import com.trello25.domain.board.service.BoardService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
    public void createBoards(@PathVariable(name = "id") long id, @Valid @RequestBody CreateBoardRequest createBoardRequest){
        boardService.createBoard(id, createBoardRequest);
    }

    /**
     * 보드 수정
     * @param id
     * @param updateBoardRequest
     */
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/boards/{id}")
    public void updateBoards(@PathVariable Long id, @Valid @RequestBody UpdateBoardRequest updateBoardRequest){
        boardService.updateBoard(id, updateBoardRequest);
    }

    /**
     * 보드 목록 조회
     * @param id
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/workspaces/{id}/boards")
    public List<BoardResponse> getBoardList(@PathVariable Long id){
        List<BoardResponse> responseList = boardService.getBoardList(id);
        return responseList;
    }

//    /**
//     * 보드 단건 조회
//     * @param id
//     * @return
//     */
//    @ResponseStatus(HttpStatus.OK)
//    @GetMapping("/boards/{id}")
//    public BoardResponse getBoard(@PathVariable Long id){
//        return boardService.getBoard(id);
//    }

    /**
     * 보드 삭제
     */
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/boards/{id}")
    public void deleteBoard(@PathVariable Long id){
        boardService.deleteBoard(id);
    }

}
