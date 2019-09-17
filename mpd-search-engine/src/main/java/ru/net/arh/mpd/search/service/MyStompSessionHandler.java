package ru.net.arh.mpd.search.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Component;
import ru.net.arh.mpd.search.model.SockJSResponse;
import ru.net.arh.mpd.search.model.TreeItem;

import java.lang.reflect.Type;

import static ru.net.arh.mpd.search.model.EventType.FULL_TREE;
import static ru.net.arh.mpd.search.model.EventType.SOCKS_CONNECTED;
import static ru.net.arh.mpd.search.model.EventType.SOCKS_DISCONNECTED;

@Component
@Slf4j
public class MyStompSessionHandler implements StompSessionHandler {

    @Autowired
    private ApplicationEventPublisher publisher;

    private StompSession session;

    private void checkConnection() {
        session.send("/mpd/connectionState", null);
    }

    private void askFullTree() {
        session.send("/mpd/fullTree", null);
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        session.subscribe("/topic/fullTree", this);
        session.subscribe("/topic/connection", this);
        session.subscribe("/user/queue/reply", this);
        this.session=session;
        publisher.publishEvent(SOCKS_CONNECTED.event(null));
        checkConnection();
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        log.warn(exception.getStackTrace().toString());
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        publisher.publishEvent(SOCKS_DISCONNECTED.event(null));

    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return SockJSResponse.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        if (!(payload instanceof SockJSResponse)) return;
        Object payload1 = ((SockJSResponse) payload).getPayload();
        if (payload1 instanceof Boolean) {
            if ((Boolean) payload1) {
                askFullTree();
            }
        }
        if (payload1 instanceof TreeItem) {
            publisher.publishEvent(FULL_TREE.event(payload1));
        }
    }
}
