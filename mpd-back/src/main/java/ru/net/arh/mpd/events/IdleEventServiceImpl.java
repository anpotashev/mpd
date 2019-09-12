package ru.net.arh.mpd.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;
import ru.net.arh.mpd.model.MpdIdleType;
import ru.net.arh.mpd.model.events.MpdEvent;
import ru.net.arh.mpd.model.events.MpdEventType;
import ru.net.arh.mpd.model.events.MpdIdleEventMethod;
import ru.net.arh.mpd.model.events.MpdIldeEventClass;
import ru.net.arh.mpd.stomp.WsSubscribersService;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Класс с обработчиками событий idle. Очищают нужные кэши, иницируют вызовы методов запросов данных, которые могли поменяться и публикуют
 * события, содержащие обновленные данные
 */
@Service
@Slf4j
public class IdleEventServiceImpl implements IdleEventService {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private CacheManager cacheManager;

    private Map<MpdIdleType, MpdIdleAction> registr = new HashMap<>();

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private WsSubscribersService wsSubscribersService;

    @Override
    public void processIdleEvent(MpdIdleType type) {
        MpdIdleAction action = registr.getOrDefault(type, null);
        if (action == null) {
            return;
        }

        List<String> cacheNames = action.getCacheNames();
        evictCaches(cacheNames);
        action.actionMethods.forEach(mpdIdleActionMethod -> {
            if (wsSubscribersService.isSubscribersFound(mpdIdleActionMethod.getType()
                                                                .getDestionation())) { //Если нет подписчиков, то и не надо делать лишнюю работу
                try {
                    Object result = mpdIdleActionMethod.getMethod().invoke(mpdIdleActionMethod.getBean());
                    publisher.publishEvent(new MpdEvent(mpdIdleActionMethod.getType(), result));
                } catch (IllegalAccessException | InvocationTargetException e) {

                }
            }
        });
    }

    private void evictCaches(List<String> cacheNames) {
        cacheNames.forEach(cacheName -> cacheManager.getCache(cacheName).clear());
    }

    @PostConstruct
    public void init() {
        context.getBeansWithAnnotation(MpdIldeEventClass.class).values().stream()
                .forEach(bean -> {
                    Object bean1 = AopUtils.isAopProxy(bean) ? AopUtils.getTargetClass(bean) : bean;
                    Arrays.stream(((Class) bean1).getDeclaredMethods())
                            .filter(method -> method.isAnnotationPresent(MpdIdleEventMethod.class))
                            .forEach(method -> addToMap(bean, method));
                });
    }

    private void addToMap(Object bean, Method method) {
        String[] cacheNames = getCacheNames(method);
        MpdIdleEventMethod mpdIdleEventMethodAnnotation = method.getAnnotation(MpdIdleEventMethod.class);
        MpdIdleType[] types = mpdIdleEventMethodAnnotation.types();
        MpdEventType mpdEventType = mpdIdleEventMethodAnnotation.eventType();
        register(bean, method, cacheNames, types, mpdEventType);
    }

    /**
     * Если у метода присутствует аннотация @Cacheable, то возвращает имена кэшей из нее В противном случае, возвращает пустой массив
     */
    private String[] getCacheNames(Method method) {
        Cacheable cacheableAnnotation = AnnotationUtils.findAnnotation(method, Cacheable.class);
        return cacheableAnnotation != null
                ? cacheableAnnotation.cacheNames()
                : new String[]{};
    }

    private void register(Object bean,
                          Method method,
                          String[] cacheNames,
                          MpdIdleType[] types,
                          MpdEventType mpdEventType) {
        Arrays.stream(types)
                .forEach(mpdIdleType -> {
                    registr.putIfAbsent(mpdIdleType, new MpdIdleAction());
                    MpdIdleAction action = registr.get(mpdIdleType);
                    List<String> cacheNames1 = action.getCacheNames();
                    cacheNames1.addAll(Arrays.asList(cacheNames));
                    cacheNames1 = cacheNames1.stream().distinct().collect(Collectors.toList());
                    action.setCacheNames(cacheNames1);
                    action.getActionMethods().add(new MpdIdleActionMethod(bean, method, mpdEventType));
                });

    }

    private class MpdIdleAction {
        @Getter
        @Setter
        List<String> cacheNames = new ArrayList<>();
        @Getter
        List<MpdIdleActionMethod> actionMethods = new ArrayList<>();
    }

    @Getter
    @AllArgsConstructor
    private class MpdIdleActionMethod {
        Object bean;
        Method method;
        MpdEventType type;
    }

}
