package ru.net.arh.mpd.web;

import org.springframework.messaging.handler.HandlerMethod;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.ControllerAdvice;
import ru.net.arh.mpd.model.MpdErrorType;
import ru.net.arh.mpd.model.exception.MpdException;
import ru.net.arh.mpd.model.sockjs.MpdSockJsError;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class MpdExceptionHandler {

    /**
     * При получении ошибки у метода контроллера, в который пришел запрос, проверяется наличие аннотации MpdErrorType. При ее наличии
     * пользователю отправляется сообщение об ошибке.
     */
    @SendToUser("/queue/error")
    @MessageExceptionHandler({MpdException.class, ConstraintViolationException.class})
    public MpdSockJsError handleMpdException(Exception e, HandlerMethod handlerMethod) {
        MpdErrorType methodAnnotation = handlerMethod.getMethodAnnotation(MpdErrorType.class);
        if (methodAnnotation == null) {
            return null;
        }
        return new MpdSockJsError(methodAnnotation.type().name() + "_FAILED", e.getMessage());
    }

}
