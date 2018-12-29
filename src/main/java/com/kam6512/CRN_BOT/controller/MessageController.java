package com.kam6512.CRN_BOT.controller;

import com.kam6512.CRN_BOT.service.CrnService;
import lombok.NoArgsConstructor;
import me.ramswaroop.jbot.core.common.Controller;
import me.ramswaroop.jbot.core.common.EventType;
import me.ramswaroop.jbot.core.common.JBot;
import me.ramswaroop.jbot.core.slack.Bot;
import me.ramswaroop.jbot.core.slack.models.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.socket.WebSocketSession;

@JBot
@NoArgsConstructor
public class MessageController extends Bot {


    @Autowired
    private CrnService crnService;

    @Value("${slackBotToken}")
    private String slackToken;


    @Controller(events = {EventType.MESSAGE, EventType.DIRECT_MESSAGE})
    public void onReceiveAnyMessage(WebSocketSession session, Event event) {
        crnService.onReceivedMessage(session, event)
                .subscribe(message -> reply(session, event, message))
                .dispose();
    }

    @Override
    public String getSlackToken() {
        return slackToken;
    }

    @Override
    public Bot getSlackBot() {
        return this;
    }
}

