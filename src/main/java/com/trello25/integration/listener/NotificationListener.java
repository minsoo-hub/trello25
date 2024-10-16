package com.trello25.integration.listener;

import com.trello25.common.SlackEvent;
import com.trello25.integration.slack.SlackApi;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final SlackApi slackApi;

    @EventListener(SlackEvent.class)
    public void notify(SlackEvent slackEvent) {
        slackApi.notify(slackEvent.getMessage());
    }
}
