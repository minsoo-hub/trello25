package com.trello25.domain.board.service;

import com.trello25.domain.board.dto.request.CreateBoardRequest;
import com.trello25.domain.board.dto.request.UpdateBoardRequest;
import com.trello25.domain.board.dto.response.BoardResponse;
import com.trello25.domain.board.entity.Board;
import com.trello25.domain.board.repository.BoardRepository;
import com.trello25.domain.card.repository.CardRepository;
import com.trello25.domain.common.entity.EntityStatus;
import com.trello25.domain.kanban.repository.KanbanRepository;
import com.trello25.domain.member.entity.Member;
import com.trello25.domain.member.repository.MemberRepository;
import com.trello25.domain.member.service.MemberService;
import com.trello25.domain.workspace.entity.Workspace;
import com.trello25.domain.workspace.repository.WorkspaceRepository;
import com.trello25.exception.ApplicationException;
import com.trello25.exception.ErrorCode;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final WorkspaceRepository workspaceRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final KanbanRepository kanbanRepository;
    private final CardRepository cardRepository;
//    private final KanbanService kanbanService;

    public void createBoard(long id, CreateBoardRequest createBoardRequest) {
        //로그인 확인 여부 확인

        //워크스페이스 존재 여부 확인
        Workspace workspace = workspaceRepository.findById(id)
            .orElseThrow(()-> new ApplicationException(ErrorCode.UNAUTHORIZED_ACCESS));

        //워크스페이스 상태 확인
        if(workspace.getStatus() == EntityStatus.DELETED){
            throw new ApplicationException(ErrorCode.RESOURCE_NOT_FOUND);
        }

        // 현재 로그인한 사용자가 이 워크스페이스의 멤버인지 확인
        List<Member> members = memberRepository.findByWorkspace_Id(workspace.getId());

        if (members.isEmpty()) {
            throw new ApplicationException(ErrorCode.MEMBER_NOT_FOUND); //워크스페이스에 멤버가 없는 경우
        }

//        // 현재 로그인한 사용자가 이 워크스페이스의 멤버인지 확인
//        Member member = members.stream()
//            .filter(m -> m.getUser().getId().equals(currentUser.getId()))  // 현재 사용자의 ID와 일치하는 멤버 찾기
//            .findFirst()
//            .orElseThrow(() -> new ApplicationException(ErrorCode.MEMBER_NOT_FOUND));  // 사용자가 해당 워크스페이스의 멤버가 아닌 경우
//
//        // 읽기 전용 권한 확인
//        if (member.getPermission() == Permission.READ_ONLY) {
//            throw new ApplicationException(ErrorCode.FORBIDDEN_READ_ONLY_USER);
//        }

        //제목 비어있는지 확인
        if(createBoardRequest.getTitle() == null || createBoardRequest.getTitle().trim().isEmpty()){
            throw new ApplicationException(ErrorCode.EMPTY_BOARD_TITLE);
        }

        //보드 생성
        Board board = new Board(
            createBoardRequest.getTitle(),
            createBoardRequest.getBackColor(),
            createBoardRequest.getImagePath(),
            workspace);

        boardRepository.save(board);
    }

    public void updateBoard(Long id, UpdateBoardRequest updateBoardRequest) {
        // 보드 존재 여부 확인
        Board board = boardRepository.findById(id)
            .orElseThrow(() -> new ApplicationException(ErrorCode.RESOURCE_NOT_FOUND));

        //보드 상태 확인
        if(board.getStatus() == EntityStatus.DELETED){
            throw new ApplicationException(ErrorCode.RESOURCE_NOT_FOUND);
        }

//        // 보드에서 워크스페이스 정보 가져오기
//        Workspace workspace = board.getWorkspace();
//
//        // 현재 로그인한 사용자의 ID를 가져오기 (유저 ID가 아니라 워크스페이스에 연결된 멤버를 확인할 것임)
//        Long memberId = memberService.getLoggedInMemberId()
//            .orElseThrow(() -> new ApplicationException(ErrorCode.UNAUTHORIZED_ACCESS));
//
//        // 멤버가 해당 워크스페이스에 속해 있는지 확인
//        Member member = memberRepository.findByIdAndWorkspaceId(memberId, workspace.getId())
//            .orElseThrow(() -> new ApplicationException(ErrorCode.MEMBER_NOT_FOUND));
//
//        // 읽기 전용 권한 확인
//        if (member.getPermission() == Permission.READ_ONLY) {
//            throw new ApplicationException(ErrorCode.FORBIDDEN_READ_ONLY_USER);
//        }

        //제목 비어있는지 확인
        if(updateBoardRequest.getTitle() == null || updateBoardRequest.getTitle().trim().isEmpty()){
            throw new ApplicationException(ErrorCode.EMPTY_BOARD_TITLE);
        }

        //보드 수정
        board.updateBoard(updateBoardRequest.getTitle(),
            updateBoardRequest.getBackColor(),
            updateBoardRequest.getImagePath());

        boardRepository.save(board);
    }

    @Transactional(readOnly = true)
    public java.util.List<BoardResponse> getBoardList(Long id) {
        // 워크스페이스 존재 여부 확인
        if(!workspaceRepository.existsById(id)){
            throw new ApplicationException(ErrorCode.RESOURCE_NOT_FOUND);
        }

        // 보드 목록 조회
        java.util.List<Board> boards = boardRepository.findByWorkspace_IdAndStatus(id, EntityStatus.ACTIVATED);

        // 보드 목록이 비어 있는 경우 확인
        if(boards.isEmpty()){
            throw new ApplicationException(ErrorCode.RESOURCE_NOT_FOUND);
        }

        return boards.stream()
            .map(board -> new BoardResponse(
                board.getId(),
                board.getWorkspace().getId(),
                board.getTitle(),
                board.getBackColor()
            ))
            .collect(Collectors.toList());
    }

//    @Transactional(readOnly = true)
//    public BoardResponse getBoard(Long id) {
//        //보드 존재 여부 확인
//        Board board = boardRepository.findById(id)
//            .orElseThrow(() -> new ApplicationException(ErrorCode.RESOURCE_NOT_FOUND));
//
//        //보드 상태 확인
//        if(board.getStatus() == EntityStatus.DELETED){
//            throw new ApplicationException(ErrorCode.RESOURCE_NOT_FOUND);
//        }
//
//        // 리스트 목록 조회
//        java.util.List<KanbanResponse> kanbanResponses= kanbanService.getKanbans(board.getId());
//
//
////        java.util.List<ListResponse> listResponseList = lists.stream()
////            .map(ListResponse::new)
////            .collect(Collectors.toList());
////
////        // 카드 목록 조회
////        java.util.List<Card> cardList = cardRepository.findActiByBoardId(id);
////
////        java.util.List<CardResponse> cardResponseList = cardList.stream()
////            .map(card -> new CardResponse(card))
////            .collect(Collectors.toList());
//
//        return new BoardResponse(
//            board.getId(),
//            board.getTitle(),
//            board.getBackColor(),
//            kanbanResponses
//         );
//    }

    public void deleteBoard(Long id) {
        // 보드 존재 여부 확인
        Board board = boardRepository.findById(id)
            .orElseThrow(() -> new ApplicationException(ErrorCode.RESOURCE_NOT_FOUND));

        //보드 상태 확인
        if(board.getStatus() == EntityStatus.DELETED){
            throw new ApplicationException(ErrorCode.RESOURCE_NOT_FOUND);
        }

        //읽기 전용 권한 확인

        board.delete();
    }
}
