package ru.net.arh.mpd.stomp;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.messaging.simp.SimpMessageHeaderAccessor.DESTINATION_HEADER;
import static org.springframework.messaging.simp.SimpMessageHeaderAccessor.SESSION_ID_HEADER;

@Service
public class WsSubscribersServiceImpl implements WsSubscribersService {

    private Map<String, Set<String>> subscriptions = new ConcurrentHashMap();

    @Override
    public boolean isSubscribersFound(String destination) {
        return subscriptions.values().stream().filter(strings -> strings.contains(destination)).findAny().isPresent();
    }

    @EventListener
    private void onSessionSubscribeEvent(SessionSubscribeEvent event) {
        String simpSessionId = event.getMessage().getHeaders().get(SESSION_ID_HEADER, String.class);
        String simpDestination = event.getMessage().getHeaders().get(DESTINATION_HEADER, String.class);
        subscriptions.putIfAbsent(simpSessionId, new HashSet<>());
        subscriptions.get(simpSessionId).add(simpDestination);
    }

    @EventListener
    private void onSessionUnsubscribeEvent(SessionUnsubscribeEvent event) {
        String simpSessionId = event.getMessage().getHeaders().get(SESSION_ID_HEADER, String.class);
        String simpDestination = event.getMessage().getHeaders().get(DESTINATION_HEADER, String.class);
        subscriptions.getOrDefault(simpSessionId, new HashSet<>()).remove(simpDestination);
    }

    @EventListener
    private void onSessionDisconnectEvent(SessionDisconnectEvent event) {
        String simpSessionId = event.getMessage().getHeaders().get(SESSION_ID_HEADER, String.class);
        subscriptions.remove(simpSessionId);
    }

}
