package com.trello25.integration.slack;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.trello25.common.AppProfile;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AppProfile
public class DefaultSlackApi implements SlackApi {

    private static final String CHANNEL_NAME = "프로젝트";

    private final MethodsClient methodsClient;

    public DefaultSlackApi(@Value(value = "${slack.token}") String slackToken) {
        this.methodsClient = Slack.getInstance().methods(slackToken);
    }

    @Override
    public void notify(String message) {
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
