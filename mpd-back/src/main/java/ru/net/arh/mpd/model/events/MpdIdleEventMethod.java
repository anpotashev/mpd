package ru.net.arh.mpd.model.events;

import ru.net.arh.mpd.model.MpdIdleType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Методы помеченные этой аннотацией вызываются при получении события с типом указанном в types() Если на методе есть аннотация @Cacheable,
 * то кэши указанные в ней будут очищены Для обработки класс с методом должен быть помечен аннотацией @See
 * ru.net.arh.mpd.model.events.MpdIdleEventClass
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MpdIdleEventMethod {
    MpdIdleType[] types();

    MpdEventType eventType();

    boolean alwaysUpdate() default false;

}
