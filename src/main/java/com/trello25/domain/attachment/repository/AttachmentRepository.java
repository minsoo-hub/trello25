package com.trello25.domain.attachment.repository;

import com.trello25.domain.attachment.entity.Attachment;
import com.trello25.domain.card.entity.Card;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    List<Attachment> findAllByCard(Card card);
}
