package ru.net.arh.mpd.search.service;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Component;
import ru.net.arh.mpd.search.model.SockJSResponse;

import java.lang.reflect.Type;

@Component
public class MyStompSessionHandler implements StompSessionHandler {
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        session.subscribe("/topic/fullTree", this);
        session.subscribe("/topic/connection", this);
        session.subscribe("/user/queue/reply", this);
        session.send("/mpd/fullTree", null);
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {

    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {

    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return SockJSResponse.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {

    }
}
