package ru.net.arh.mpd.model.events;

import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация над классом помечает его для поиска в нем методов аннотированных @See ru.net.arh.mpd.model.events.MpdIldeEventMethod
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Service
public @interface MpdIldeEventClass {
}
