package ru.net.arh.mpd.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ru.net.arh.mpd.model.MpdIdleType;
import ru.net.arh.mpd.model.events.MpdEvent;
import ru.net.arh.mpd.model.events.MpdEventType;

@Service
public class EventsServiceImpl implements EventsService {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private CacheManager cacheManager;

    @Autowired IdleEventService idleEventService;

    @Override
    public void onConnect() {
        cacheManager.getCacheNames().stream()
                .forEach(cacheName -> cacheManager.getCache(cacheName).clear());
        publisher.publishEvent(new MpdEvent<>(MpdEventType.CONNECTION_STATE_CHANGED, Boolean.TRUE));
    }

    @Override
    public void onDisconnect() {
        publisher.publishEvent(new MpdEvent<>(MpdEventType.CONNECTION_STATE_CHANGED, Boolean.FALSE));
    }

    @Override
    public void onIdle(String system) {
        idleEventService.processIdleEvent(MpdIdleType.fromString(system));
    }
}
