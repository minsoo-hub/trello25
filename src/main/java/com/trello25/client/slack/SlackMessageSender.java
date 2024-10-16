package com.trello25.client.slack;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SlackMessageSender {

    public static final String CHANNEL_NAME = "프로젝트";

    private final MethodsClient methodsClient;

    public SlackMessageSender(@Value(value = "${slack.token}") String slackToken) {
        this.methodsClient = Slack.getInstance().methods(slackToken);
    }

    public void sendMessage(String message) {
        try {
            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(CHANNEL_NAME)
                    .text(message)
                    .build();

            methodsClient.chatPostMessage(request);
        } catch (SlackApiException | IOException e) {
            log.error("슬랙 메시지 전송 실패, ", e);
        }
    }
}
