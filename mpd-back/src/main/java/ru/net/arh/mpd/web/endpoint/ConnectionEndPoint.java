package ru.net.arh.mpd.web.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import ru.net.arh.mpd.connection.ConnectionService;
import ru.net.arh.mpd.events.MpdEvent;
import ru.net.arh.mpd.model.sockjs.ResponseType;
import ru.net.arh.mpd.model.sockjs.SockJsResponse;

@Controller
public class ConnectionEndPoint {
    @Autowired
    private ConnectionService connectionService;
    @MessageMapping("/connectionState")
    @SendToUser("/queue/reply")
    public SockJsResponse<Boolean> isConnected() {
        return new SockJsResponse<>(ResponseType.CONNECTION_STATE, connectionService.isConnected());
    }

    @MessageMapping("/connect")
    public void connect() {
        connectionService.connect();
    }

    @MessageMapping("/disconnect")
    public void disconnect() {
        connectionService.disconnect();
    }

    @SendTo("/topic/connection")
    @EventListener(condition = "#event.type == T(ru.net.arh.mpd.events.MpdEventType).CONNECTION_STATE_CHANGED")
    public SockJsResponse<Boolean> handleConnectionStateChanged(MpdEvent event) {
        return new SockJsResponse<>(ResponseType.CONNECTION_STATE, connectionService.isConnected());
    }

    @SendTo("/topic/connection1")
    @Scheduled(fixedDelay = 1000)
    public SockJsResponse<Boolean> test() {
        return new SockJsResponse<>(ResponseType.CONNECTION_STATE, connectionService.isConnected());
    }


}
