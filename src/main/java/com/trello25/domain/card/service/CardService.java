package com.trello25.domain.card.service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.trello25.domain.attachment.dto.response.AttachmentResponse;
import com.trello25.domain.attachment.entity.Attachment;
import com.trello25.domain.attachment.repository.AttachmentRepository;
import com.trello25.domain.card.dto.request.CreateCardRequest;
import com.trello25.domain.card.dto.request.DeleteCardRequest;
import com.trello25.domain.card.dto.request.UpdateCardRequest;
import com.trello25.domain.card.dto.response.CardDetailResponse;
import com.trello25.domain.card.dto.response.SearchCardResponse;
import com.trello25.domain.card.entity.Card;
import com.trello25.domain.card.repository.CardRepository;
import com.trello25.domain.cardactive.actiontype.ActionType;
import com.trello25.domain.cardactive.dto.CardActiveResponse;
import com.trello25.domain.cardactive.entity.CardActive;
import com.trello25.domain.cardactive.repository.CardActiveRepository;
import com.trello25.domain.comment.dto.CommentResponse;
import com.trello25.domain.common.entity.EntityStatus;
import com.trello25.domain.kanban.entity.Kanban;
import com.trello25.domain.kanban.repository.KanbanRepository;
import com.trello25.domain.manager.entity.Manager;
import com.trello25.domain.member.entity.Member;
import com.trello25.domain.member.entity.Permission;
import com.trello25.domain.member.repository.MemberRepository;
import com.trello25.exception.ApplicationException;
import com.trello25.exception.ErrorCode;
import java.time.LocalDate;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final KanbanRepository kanbanRepository;
    private final MemberRepository memberRepository;
    private final CardActiveRepository cardActiveRepository;
    private final AmazonS3 s3Client;
    private final AttachmentRepository attachmentRepository;

    public void createCard(CreateCardRequest createCardRequest) {

        if (createCardRequest.getDeadline().isBefore(LocalDate.now())) {
            throw new ApplicationException(ErrorCode.INVALID_DEADLINE);
        }

        Kanban kanban = kanbanRepository.findByIdAndStatus(createCardRequest.getKanbanId(),
                        EntityStatus.ACTIVATED)
                .orElseThrow(() -> new ApplicationException(ErrorCode.KANBAN_NOT_FOUND));

        Member author = memberRepository.findByIdAndStatusOrThrow(createCardRequest.getMemberId(),
                EntityStatus.ACTIVATED);

        if (author.getPermission().equals(Permission.READ_ONLY)) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        Card card = new Card(createCardRequest.getTitle(), createCardRequest.getDescription(),
                createCardRequest.getDeadline(), kanban);
        List<Manager> managerList = memberRepository.findAllByIdIn(createCardRequest.getManagers())
                .stream()
                .map(member -> new Manager(card, member))
                .toList();

        for (Manager manager : managerList) {
            card.addManager(manager);
        }

        cardRepository.save(card);
        CardActive cardActive = new CardActive(card, author, ActionType.CREATE);
        cardActiveRepository.save(cardActive);
        card.addCardActive(cardActive);
    }

    public void updateCard(Long id, UpdateCardRequest updateCardRequest) {

        Card card = cardRepository.findByIdAndStatusOrThrow(id, EntityStatus.ACTIVATED);

        Member author = memberRepository.findByIdAndStatusOrThrow(updateCardRequest.getMemberId(),
                EntityStatus.ACTIVATED);

        if (author.getPermission().equals(Permission.READ_ONLY)) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        if (updateCardRequest.getDeadline().isBefore(LocalDate.now())) {
            throw new ApplicationException(ErrorCode.INVALID_DEADLINE);
        }

        if (updateCardRequest.getTitle() != null) {
            card.updateTitle(updateCardRequest.getTitle());
        }

        if (updateCardRequest.getDescription() != null) {
            card.updateDescription(updateCardRequest.getDescription());
        }

        if (updateCardRequest.getDeadline() != null) {
            card.updateDeadline(updateCardRequest.getDeadline());
        }

        if (updateCardRequest.getManagers() != null) {
            List<Manager> managerList = memberRepository.findAllById(updateCardRequest.getManagers())
                    .stream().map(member -> new Manager(card, member)).toList();

            for (Manager manager : managerList) {
                if (card.getManagers().contains(manager)) {
                    throw new ApplicationException(ErrorCode.ALREADY_ASSIGNED_MANAGER);
                }
                card.addManager(manager);
                CardActive cardActive = new CardActive(card, author, ActionType.ASSIGN_MANAGER);
                cardActiveRepository.save(cardActive);
                card.addCardActive(cardActive);
            }
        }

        cardRepository.save(card);
        CardActive cardActive = new CardActive(card, author, ActionType.UPDATE);
        cardActiveRepository.save(cardActive);
        card.addCardActive(cardActive);
    }

    @Transactional(readOnly = true)
    public CardDetailResponse getCard(Long id) {
        if (!cardRepository.existsById(id)) {
            throw new ApplicationException(ErrorCode.CARD_NOT_FOUND);
        }

        Card card = cardRepository.findCardWithCardActiveAndComment(id);

        List<CardActiveResponse> cardActiveResponses = card.getCardActives().stream()
                .map(cardActive -> new CardActiveResponse(cardActive.getId(),
                        cardActive.getMember().getId(), cardActive.getAction()))
                .toList();

        List<CommentResponse> commentResponses = card.getComments().stream()
                .map(comment -> new CommentResponse(comment.getId(), comment.getContent()))
                .toList();

        return new CardDetailResponse(card, cardActiveResponses, commentResponses);
    }

    public void deleteCard(Long id, DeleteCardRequest deleteCardRequest) {

        Card card = cardRepository.findByIdAndStatusOrThrow(id, EntityStatus.ACTIVATED);
        Member author = memberRepository.findByIdAndStatus(deleteCardRequest.getMemberId(),EntityStatus.ACTIVATED)
                .orElseThrow(() -> new ApplicationException(ErrorCode.MEMBER_NOT_FOUND));

        if (author.getPermission().equals(Permission.READ_ONLY)) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        card.delete();
        cardRepository.save(card);
        CardActive cardActive = new CardActive(card, author, ActionType.DELETE);
        cardActiveRepository.save(cardActive);
        card.addCardActive(cardActive);
    }

    public Page<SearchCardResponse> getCardsByConditions(Long id, String title,
            String description, LocalDate deadline, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return cardRepository.findCardsByConditions(id,title,description,deadline,pageable);

    }

    //파일 첨부
    public void attachFile(Long currentUserId, Long cardId,Long memberId, MultipartFile file) throws IOException{
        // 읽기 권한 확인
        Member author = memberRepository.findByIdAndStatusOrThrow(memberId,
            EntityStatus.ACTIVATED);

        if (author.getPermission().equals(Permission.READ_ONLY)) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        // 이미지 바이트 코드 가져오기
        InputStream imageInputStream = file.getInputStream();

        // 사용자가 보낸 파일 이름 -> board.originName
        String originalFileName = file.getOriginalFilename();

        // s3에 저장될 파일 이름
        UUID uuid = UUID.randomUUID();
        String subString = getSubString(file);

        // 지원되는 파일 형식 확인 (이미지: jpg, png, 문서: pdf, csv)
        if (!isValidFileType(subString)) {
            throw new ApplicationException(ErrorCode.INVALID_FILE_TYPE);
        }

        // S3에 저장될 파일 이름 생성
        String s3FileName = uuid + getSubString(file);

        // 메타 데이터
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());

        // S3에 파일 업로드
        s3Client.putObject(new PutObjectRequest("nbc.trello", s3FileName, imageInputStream, objectMetadata));

        // s3 url 생성
        String urlPath = s3Client.getUrl("nbc.trello", s3FileName).toString();

        // cardId로 Card 객체 조회
        Card card = cardRepository.findById(cardId)
            .orElseThrow(() -> new ApplicationException(ErrorCode.CARD_NOT_FOUND));

        Attachment attachment = new Attachment(originalFileName, urlPath, card);
        attachmentRepository.save(attachment);

    }

    //파일 첨부 목록 조회
    public List<AttachmentResponse> getAttachmentsByCardId(Long cardId){
        //cardId로 Card 객체 조회
        Card card = cardRepository.findById(cardId)
            .orElseThrow(()-> new ApplicationException(ErrorCode.CARD_NOT_FOUND));

        // 카드에 속한 첨부파일 목록 조회
        List<Attachment> attachmentList = attachmentRepository.findAllByCard(card);

        return attachmentList.stream()
            .map(attachment -> new AttachmentResponse(attachment.getOriginalFileName(), attachment.getUrlPath()))
            .toList();
    }

    public void deleteAttachment(Long attachmentId, Long memberId){
        // 읽기 권한 확인
        Member author = memberRepository.findByIdAndStatusOrThrow(memberId,
            EntityStatus.ACTIVATED);

        if (author.getPermission().equals(Permission.READ_ONLY)) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        //attachmentId로 첨부파일 객체 조회
        Attachment attachment = attachmentRepository.findById(attachmentId)
            .orElseThrow(()-> new ApplicationException(ErrorCode.ATTACHMENT_NOT_FOUND));

        //s3에서 파일 삭제
        String s3FileName = attachment.getUrlPath().substring(attachment.getUrlPath().lastIndexOf("/")+1);
        s3Client.deleteObject("nbc.trello", s3FileName);

        // 데이터베이스에서 첨부파일 삭제
        attachmentRepository.delete(attachment);
    }

    private String getSubString(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        int lastIndex = originalFileName.lastIndexOf("."); // 제일 마지막 .위치 가져오기
        return originalFileName.substring(lastIndex); //.부터 글자 가져오기
    }

    // 지원되는 파일 형식 확인 메서드
    private boolean isValidFileType(String fileExtension) {
        // 지원되는 파일 확장자 목록
        List<String> validExtensions = Arrays.asList(".jpg", ".png", ".pdf", ".csv");
        return validExtensions.contains(fileExtension.toLowerCase());
    }

}
