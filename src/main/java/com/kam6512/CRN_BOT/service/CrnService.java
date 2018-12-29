package com.kam6512.CRN_BOT.service;

import io.reactivex.Observable;
import me.ramswaroop.jbot.core.slack.models.Event;
import me.ramswaroop.jbot.core.slack.models.Message;
import org.springframework.web.socket.WebSocketSession;

public interface CrnService {
    Observable<Message> onReceivedMessage(WebSocketSession session, Event event);
}

