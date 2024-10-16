package com.trello25.domain.board.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.trello25.domain.board.dto.request.CreateBoardRequest;
import com.trello25.domain.board.dto.request.UpdateBoardRequest;
import com.trello25.domain.board.dto.response.BoardResponse;
import com.trello25.domain.board.entity.Board;
import com.trello25.domain.board.repository.BoardRepository;
import com.trello25.domain.common.entity.EntityStatus;
import com.trello25.domain.kanban.dto.response.KanbanResponse;
import com.trello25.domain.kanban.service.KanbanService;
import com.trello25.domain.member.entity.Member;
import com.trello25.domain.member.entity.Permission;
import com.trello25.domain.member.repository.MemberRepository;
import com.trello25.domain.workspace.entity.Workspace;
import com.trello25.domain.workspace.repository.WorkspaceRepository;
import com.trello25.exception.ApplicationException;
import com.trello25.exception.ErrorCode;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final WorkspaceRepository workspaceRepository;
    private final MemberRepository memberRepository;
    private final KanbanService kanbanService;
    private final AmazonS3 s3Client;

    @Value("${background.default.image}")
    private String DEFAULT_IMAGE;

    public void createBoard(Long currentUserId, long id, CreateBoardRequest createBoardRequest) {
        //워크스페이스 존재 여부 확인
        Workspace workspace = workspaceRepository.findById(id)
            .orElseThrow(()-> new ApplicationException(ErrorCode.LOGIN_REQUIRED));

        //워크스페이스 상태 확인
        if(workspace.getStatus() == EntityStatus.DELETED){
            throw new ApplicationException(ErrorCode.RESOURCE_NOT_FOUND);
        }

        // 멤버가 해당 워크스페이스에 속해 있는지 확인
        Member member = getMemberByUserIdAndWorkspaceId(currentUserId, workspace);

        // 읽기 전용 권한이 아닌지 확인
        validateNotReadOnlyMember(member);

        //보드 생성
        Board board = new Board(
            createBoardRequest.getTitle(),
            createBoardRequest.getBackColor(),
            DEFAULT_IMAGE,
            workspace);

        boardRepository.save(board);
    }

    public void updateBoard(Long currentUserId, Long id, UpdateBoardRequest updateBoardRequest) {
        // 보드 존재 여부 확인
        Board board = getBoardById(id);

        // 삭제된 보드인지 확인
        validateDeletedBoard(board);

        // 보드의 워크스페이스 정보 가져오기
        Workspace workspace = board.getWorkspace();

        // 멤버가 해당 워크스페이스에 속해 있는지 확인
        Member member = getMemberByUserIdAndWorkspaceId(currentUserId, workspace);

        // 읽기 전용 권한이 아닌지 확인
        validateNotReadOnlyMember(member);

        //보드 수정
        board.updateBoard(updateBoardRequest.getTitle(),
            updateBoardRequest.getBackColor());

        boardRepository.save(board);
    }

    @Transactional(readOnly = true)
    public java.util.List<BoardResponse> getBoardList(Long currentUserId, Long id) {
        //워크스페이스 있는지 확인
        Workspace workspace = workspaceRepository.findById(id)
            .orElseThrow(() -> new ApplicationException(ErrorCode.RESOURCE_NOT_FOUND));

        // 유저가 해당 워크스페이스에 속해 있는지 확인
         getMemberByUserIdAndWorkspaceId(currentUserId, workspace);

        // 보드 목록 조회
        java.util.List<Board> boards = boardRepository.findByWorkspace_IdAndStatus(id, EntityStatus.ACTIVATED);

        return boards.stream()
            .map(board -> new BoardResponse(
                board.getId(),
                board.getWorkspace().getId(),
                board.getTitle(),
                board.getBackColor()
            ))
            .toList();
    }


    @Transactional(readOnly = true)
    public BoardResponse getBoard(Long currentUserId, Long id) {
        // 보드 존재 여부 확인
        Board board = getBoardById(id);

        // 삭제된 보드인지 확인
        validateDeletedBoard(board);

        // 보드의 워크스페이스 정보 가져오기
        Workspace workspace = board.getWorkspace();

        // 멤버가 해당 워크스페이스에 속해 있는지 확인
        getMemberByUserIdAndWorkspaceId(currentUserId, workspace);

        // 칸반 목록 조회
        java.util.List<KanbanResponse> kanbanResponses= kanbanService.getKanbans(board.getId());

        return new BoardResponse(
            board.getId(),
            board.getWorkspace().getId(),
            board.getTitle(),
            board.getBackColor(),
            kanbanResponses
         );
    }

    private Board getBoardById(Long id) {
        return boardRepository.findById(id)
            .orElseThrow(() -> new ApplicationException(ErrorCode.RESOURCE_NOT_FOUND));
    }

    // 1. 로그인한 사용자가 해당 워크스페이스에 속해있는지 확인
    // 2. 권한이 READ_ONLY면 불가능

    public void deleteBoard(Long currentUserId, Long boardId) {
        // 보드 존재 여부 확인
        Board board = getBoardById(boardId);

        // 삭제된 보드인지 확인
        validateDeletedBoard(board);

        // 보드의 워크스페이스 정보 가져오기
        Workspace workspace = board.getWorkspace();

        // 멤버가 해당 워크스페이스에 속해 있는지 확인
        Member member = getMemberByUserIdAndWorkspaceId(currentUserId, workspace);

        // 읽기 전용 권한이 아닌지 확인
        validateNotReadOnlyMember(member);

        board.delete();
        boardRepository.save(board);
    }

    private Member getMemberByUserIdAndWorkspaceId(Long currentUserId, Workspace workspace) {
        return memberRepository.findByUser_IdAndWorkspace_Id(currentUserId, workspace.getId())
            .orElseThrow(() -> new ApplicationException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private boolean isReadOnlyMember(Member member) {
        return Permission.READ_ONLY.equals(member.getPermission());
    }

    private void validateNotReadOnlyMember(Member member) {
        if (isReadOnlyMember(member)) {
            throw new ApplicationException(ErrorCode.FORBIDDEN_READ_ONLY_USER);
        }
    }

    private boolean isDeletedBoard(Board board) {
        return EntityStatus.DELETED.equals(board.getStatus());
    }

    private void validateDeletedBoard(Board board) {
        if (isDeletedBoard(board)) {
            throw new ApplicationException(ErrorCode.RESOURCE_NOT_FOUND);
        }
    }

    public void updateImage(Long currentUserId, Long boardId, MultipartFile file) throws IOException {
        // 보드 존재 여부 확인
        Board board = getBoardById(boardId);

        // 삭제된 보드인지 확인
        validateDeletedBoard(board);

        // 보드의 워크스페이스 정보 가져오기
        Workspace workspace = board.getWorkspace();

        // 멤버가 해당 워크스페이스에 속해 있는지 확인
        Member member = getMemberByUserIdAndWorkspaceId(currentUserId, workspace);

        // 읽기 전용 권한 확인
        validateNotReadOnlyMember(member);

        // 이미지 바이트 코드 가져오기
        InputStream imageInputStream = file.getInputStream();

        // 사용자가 보낸 이미지 이름 -> board.originName
        String originalFilename = file.getOriginalFilename();

        // s3에 저장될 이미지 이름
        UUID uuid = UUID.randomUUID();
        String s3ImageName = uuid + getSubString(file);

        // 메타 데이터
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());

        s3Client.putObject(new PutObjectRequest("nbc.trello", s3ImageName, imageInputStream, objectMetadata));
        String urlPath = s3Client.getUrl("nbc.trello", s3ImageName).toString();

        board.updateBackground(originalFilename, urlPath);
        boardRepository.save(board);
    }

    private static String getSubString(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        int lastIndex = originalFilename.lastIndexOf("."); // 제일 마지막 .위치 가져오기
        return originalFilename.substring(lastIndex); //.부터 글자 가져오기
    }
}
