package com.trello25.card;

import com.trello25.domain.board.entity.Board;
import com.trello25.domain.board.enums.BackColors;
import com.trello25.domain.board.repository.BoardRepository;
import com.trello25.domain.card.dto.request.CreateCardRequest;
import com.trello25.domain.card.dto.request.UpdateCardRequest;
import com.trello25.domain.card.entity.Card;
import com.trello25.domain.card.repository.CardRepository;
import com.trello25.domain.card.service.CardService;
import com.trello25.domain.cardactive.repository.CardActiveRepository;
import com.trello25.domain.common.entity.EntityStatus;
import com.trello25.domain.kanban.entity.Kanban;
import com.trello25.domain.kanban.repository.KanbanRepository;
import com.trello25.domain.member.entity.Member;
import com.trello25.domain.member.entity.Permission;
import com.trello25.domain.member.repository.MemberRepository;
import com.trello25.domain.user.entity.User;
import com.trello25.domain.user.enums.UserRole;
import com.trello25.domain.user.repository.UserRepository;
import com.trello25.domain.user.service.UserService;
import com.trello25.domain.workspace.entity.Workspace;
import com.trello25.domain.workspace.repository.WorkspaceRepository;
import com.trello25.exception.ApplicationException;
import com.trello25.exception.ErrorCode;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("local-test")

public class CardServiceConcurrencyTest {

    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private KanbanRepository kanbanRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CardActiveRepository cardActiveRepository;
    @Autowired
    private WorkspaceRepository workspaceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private CardService cardService;

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    private Card testCard;
    private Member testMember;

    @BeforeEach
    public void setup() {
        // 1. User 생성 및 저장
        User testUser = new User("testh4h4@example.com", "password", UserRole.ADMIN);
        User Saveduser = userRepository.save(testUser);  // User 저장

        // 2. Workspace 생성 및 저장
        Workspace testWorkspace = new Workspace("Test Workspace", "Test Description");
        testWorkspace = workspaceRepository.save(testWorkspace);  // Workspace 저장

        // 3. User와 Workspace를 사용하여 Member 생성 및 저장
        testMember = new Member(Permission.BOARD_MEMBER, testWorkspace, Saveduser);
        memberRepository.save(testMember);  // Member 저장

        // 4. Board 생성 및 저장
        Board testBoard = new Board("Test Board", BackColors.BLUE, null, testWorkspace);
        testBoard = boardRepository.save(testBoard);  // Board 저장

        // 5. Kanban 생성 및 저장
        Kanban testKanban = new Kanban(testBoard, "Test Kanban");
        kanbanRepository.save(testKanban);  // Kanban 저장

        testCard= new Card("Card", "Test description", LocalDate.now(), testKanban);
        cardRepository.save(testCard);  //Card저장
    }

    @Test
    public void 동시성_카드_업데이트_테스트_락_적용_전() throws InterruptedException, ExecutionException {
        UpdateCardRequest updateRequest1 = new UpdateCardRequest(testMember.getId(), "Updated Title 1", "Updated Description 1", LocalDate.now().plusDays(1), null);
        UpdateCardRequest updateRequest2 = new UpdateCardRequest(testMember.getId(), "Updated Title 2", "Updated Description 2", LocalDate.now().plusDays(2), null);

        // 두 개의 동시성 작업 실행
        Callable<Void> task1 = () -> {
            cardService.updateCard(testCard.getId(), updateRequest1);
            return null;
        };

        Callable<Void> task2 = () -> {
            cardService.updateCard(testCard.getId(), updateRequest2);
            return null;
        };

        // 작업 실행
        Future<Void> future1 = executorService.submit(task1);
        Future<Void> future2 = executorService.submit(task2);

        // 결과 대기
        future1.get();
        future2.get();

        // 카드 업데이트가 두 번 호출되었는지 검증
        assertThat(cardRepository.findById(testCard.getId()).get().getTitle()).isIn("Updated Title 1", "Updated Title 2");
        assertThat(cardRepository.findById(testCard.getId()).get().getDescription()).isIn("Updated Description 1", "Updated Description 2");
    }
}
