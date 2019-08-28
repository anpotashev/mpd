package ru.net.arh.mpd.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ru.net.arh.mpd.model.events.MpdEvent;
import ru.net.arh.mpd.model.events.MpdEventType;

@Service
public class EventsServiceImpl implements EventsService {

    @Autowired
    private ApplicationEventPublisher publisher;
    @Override
    public void onConnect() {
        publisher.publishEvent(new MpdEvent<>(MpdEventType.CONNECTION_STATE_CHANGED, true));
    }

    @Override
    public void onDisconnect() {
        publisher.publishEvent(new MpdEvent<>(MpdEventType.CONNECTION_STATE_CHANGED, false));
    }
}
