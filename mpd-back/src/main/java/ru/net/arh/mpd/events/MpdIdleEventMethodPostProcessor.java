package ru.net.arh.mpd.events;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.annotation.AnnotationUtils;
import ru.net.arh.mpd.model.MpdIdleType;
import ru.net.arh.mpd.model.events.MpdEventType;
import ru.net.arh.mpd.model.events.MpdIdleEventMethod;

import java.lang.reflect.Method;
import java.util.Arrays;

public class MpdIdleEventMethodPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = AopUtils.isAopProxy(bean) ? AopUtils.getTargetClass(bean) : bean.getClass();
        Arrays.stream(clazz
                .getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(MpdIdleEventMethod.class))
                .forEach(method -> add(bean, method));
        return bean;
    }

    private void add(Object bean, Method method) {
        MpdIdleEventMethod mpdIdleEventMethodAnnotation = method.getAnnotation(MpdIdleEventMethod.class);
        MpdIdleType[] types = mpdIdleEventMethodAnnotation.types();
        MpdEventType mpdEventType = mpdIdleEventMethodAnnotation.eventType();
        Cacheable cacheableAnnotation = AnnotationUtils.findAnnotation(method, Cacheable.class);
        String[] cacheNames =  cacheableAnnotation != null
                ? cacheableAnnotation.cacheNames()
                : new String[]{};
        MpdIdleEventsParamStorage.add(bean, mpdEventType, method, types, cacheNames);
    }

}
