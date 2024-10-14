package com.trello25.domain.card.controller;

import com.trello25.domain.card.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CardController {

    private final CardRepository cardRepository;
}
