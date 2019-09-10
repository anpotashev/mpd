package ru.net.arh.mpd.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

/**
 * todo Удалить. Использую для дебага.
 */
@Slf4j
public class LogginHandlerDecorator extends WebSocketHandlerDecorator {
    public LogginHandlerDecorator(WebSocketHandler delegate) {
        super(delegate);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info(message.toString());
        super.handleMessage(session, message);
    }
}
