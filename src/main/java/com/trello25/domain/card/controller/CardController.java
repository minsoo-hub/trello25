package com.trello25.domain.card.controller;


import com.trello25.domain.auth.dto.AuthUser;
import com.trello25.domain.card.dto.request.CreateCardRequest;
import com.trello25.domain.card.dto.request.DeleteCardRequest;
import com.trello25.domain.card.dto.response.CardDetailResponse;
import com.trello25.domain.card.dto.request.UpdateCardRequest;
import com.trello25.domain.card.dto.response.SearchCardResponse;
import com.trello25.domain.card.service.CardService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PostMapping("/cards")
    public void createCard(@RequestBody CreateCardRequest createCardRequest) {
        cardService.createCard(createCardRequest);
    }

    @PutMapping("/cards/{id}")
    public void updateCard(@PathVariable Long id, @RequestBody UpdateCardRequest updateCardRequest) {
        cardService.updateCard(id,updateCardRequest);
    }

    @GetMapping("/cards/{id}")
    public ResponseEntity<CardDetailResponse> getCard(@PathVariable Long id, @AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(cardService.getCard(id));
    }

    @DeleteMapping("/cards/{id}")
    public void deleteCard(@PathVariable Long id, @RequestBody DeleteCardRequest deleteCardRequest) {
        cardService.deleteCard(id, deleteCardRequest);
    }

    @GetMapping("/boards/{id}/cards/search")
    public ResponseEntity<Page<SearchCardResponse>> getCardsByConditions(
            @PathVariable Long id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) LocalDate deadline,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(cardService.getCardsByConditions(id,title,description,deadline,page,size));
    }
}
