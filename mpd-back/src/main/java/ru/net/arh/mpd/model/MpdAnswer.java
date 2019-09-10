package ru.net.arh.mpd.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для парсинга ответов mpd-сервера. Вешается на поля модели получаемого класса
 * <p>
 * Вешается на классы, при парсинге в коллекцию.
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MpdAnswer {

    /**
     * Для полей: Начало строки ответа с соответсвующим полем. Например @MpdAnswer(preffix = "Artist:"). В этом случае строка "Artist:
     * Иванов" будет спарсена в "Иванов"
     * <p>
     * Для классов: Используется при парсинге в коллекцию объектов. Если строка начинается с одного из preffix, то дальнейшие строки
     * относятся к новому объекту
     */
    String[] preffix();

}
