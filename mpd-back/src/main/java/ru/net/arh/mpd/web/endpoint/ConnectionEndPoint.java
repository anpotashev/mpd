package ru.net.arh.mpd.web.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import ru.net.arh.mpd.connection.ConnectionService;
import ru.net.arh.mpd.model.events.MpdEvent;
import ru.net.arh.mpd.model.sockjs.ResponseType;
import ru.net.arh.mpd.model.sockjs.SockJsResponse;

@Slf4j
@Controller
public class ConnectionEndPoint {
    @Autowired
    private ConnectionService connectionService;
    @Autowired
    private SimpMessagingTemplate template;

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

    @EventListener
    public void handleEvent(MpdEvent event) {
        template.convertAndSend(event.getType().getDestination(), new SockJsResponse(event.getType().getResponseType(), event.getBody()));
    }

}
