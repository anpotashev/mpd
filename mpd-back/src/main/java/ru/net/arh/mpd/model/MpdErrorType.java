package ru.net.arh.mpd.model;

import ru.net.arh.mpd.model.sockjs.ResponseType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для методов контроллера. Если в процессе обработки ws-запроса получаем исключение (MpdException), то будет возвращен ответ с
 * полем "тип" = type() + "_FAILED" Обрабоку см. @See ru.net.arh.mpd.web@MpdExceptionHandler#handleMpdException
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MpdErrorType {
    ResponseType type();
}
