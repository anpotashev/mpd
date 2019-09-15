package ru.net.arh.mpd.events;

import lombok.Getter;
import ru.net.arh.mpd.model.MpdIdleType;
import ru.net.arh.mpd.model.events.MpdEventType;

import java.lang.reflect.Method;
import java.util.*;

public class MpdIdleEventsParamStorage {

    private static Map<MpdIdleType, List<IdleMethod>> map = new HashMap<>();
    private static Map<MpdIdleType, Set<String>> cacheMap = new HashMap<>();

    static Collection<String> cacheNames(MpdIdleType type) {
        return cacheMap.get(type);
    }

    static Collection<IdleMethod> idleMethods(MpdIdleType type) {
        return map.get(type);
    }

    static void add(Object bean, MpdEventType type, Method method, MpdIdleType[] types, String[] cacheNames) {
        Arrays.stream(types).forEach(type1 -> {
            map.putIfAbsent(type1, new ArrayList<>());
            map.get(type1).add(new IdleMethod(bean, method, type));
            cacheMap.putIfAbsent(type1, new HashSet<>());
            cacheMap.get(type1).addAll(Arrays.asList(cacheNames));
        });

    }

    @Getter
    static class IdleMethod {
        private Object bean;
        private Method method;
        private MpdEventType type;

        public IdleMethod(Object bean, Method method, MpdEventType type) {
            this.bean = bean;
            this.method = method;
            this.type = type;
        }
    }

}
