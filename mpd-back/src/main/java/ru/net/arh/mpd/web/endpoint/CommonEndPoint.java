package ru.net.arh.mpd.web.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.net.arh.mpd.model.events.MpdEvent;
import ru.net.arh.mpd.model.sockjs.SockJsResponse;

@Controller
public class CommonEndPoint {

    @Autowired
    private SimpMessagingTemplate template;

    @EventListener
    public void handleEvent(MpdEvent event) {
        template.convertAndSend(event.getType().getDestination(), new SockJsResponse(event.getType().getResponseType(), event.getBody()));
    }

}
