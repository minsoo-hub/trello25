package com.trello25.integration.slack;


import com.trello25.common.LocalProfile;
import org.springframework.stereotype.Component;


@Component
@LocalProfile
public class LocalSlackApi implements SlackApi {

    // dev
    // prod
    // local
    @Override
    public void notify(String message) {
    }
}
