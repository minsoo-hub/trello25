package com.trello25.domain.kanban.event;

import com.trello25.common.SlackEvent;

public class KanbanCreatedEvent extends SlackEvent {
    public KanbanCreatedEvent(String message) {
        super(message);
    }
}
