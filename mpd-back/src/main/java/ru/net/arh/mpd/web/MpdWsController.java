package ru.net.arh.mpd.web;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.net.arh.mpd.model.events.MpdEvent;
import ru.net.arh.mpd.model.sockjs.SockJsResponse;

@Controller
public class MpdWsController {

    static final String REPLY_QUEUE = "/queue/reply";

    @Autowired
    private SimpMessagingTemplate template;

    /**
     * отправка все подписанным пользователям уведомления об изменениях. См. EventServiceImpl и IdleEventService
     */
    @EventListener
    public void handleEvent(MpdEvent event) {
        template.convertAndSend(
                event.getType().getDestionation(),
                new SockJsResponse<>(event.getType().getResponseType(), event.getBody())
                , ImmutableMap.of("TYPE", event.getType()));
    }

}
