package ru.net.arh.mpd.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import ru.net.arh.mpd.model.MpdIdleType;
import ru.net.arh.mpd.model.events.MpdEvent;
import ru.net.arh.mpd.stomp.WsSubscribersService;

/**
 * Класс с обработчиками событий idle. Очищают нужные кэши, иницируют вызовы методов запросов данных, которые могли поменяться и публикуют
 * события, содержащие обновленные данные
 */
@Service
@Slf4j
public class IdleEventServiceImpl implements IdleEventService {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private WsSubscribersService wsSubscribersService;

    @Override
    public void processIdleEvent(MpdIdleType type) {
        MpdIdleEventsParamStorage.cacheNames(type).forEach(s -> cacheManager.getCache(s).clear());
        MpdIdleEventsParamStorage.idleMethods(type).stream()
                .filter(idleMethod -> wsSubscribersService.isSubscribersFound(idleMethod.getType().getDestionation()))
                .forEach(idleMethod -> {
                    Object result = ReflectionUtils.invokeMethod(idleMethod.getMethod(), idleMethod.getBean());
                    publisher.publishEvent(new MpdEvent<>(idleMethod.getType(), result));
                });
    }

}
