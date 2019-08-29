package ru.net.arh.mpd.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Методы помеченные этой аннотацией бросят исключение, будучи вызваными при отсутсвии "коннекта"
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ThrowIfNotConnected {

}