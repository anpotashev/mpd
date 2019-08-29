package ru.net.arh.mpd.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.net.arh.mpd.connection.ConnectionService;
import ru.net.arh.mpd.model.exception.MpdException;

@Aspect
@Component
public class MpdAspect {


    private final ConnectionService connectionService;

    @Autowired
    public MpdAspect(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    /**
     * Если коннект отсутствует, то сразу бросаем ошибку, без отправки на выполнение.
     */
    @Before(value = "@annotation(ru.net.arh.mpd.aop.ThrowIfNotConnected)")
    public void throwIfNotConnected() {
        if (!connectionService.isConnected()) {
            throw new MpdException("not connected");
        }
    }
}