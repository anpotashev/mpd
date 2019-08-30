package ru.net.arh.mpd.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import ru.net.arh.mpd.connection.ConnectionService;
import ru.net.arh.mpd.model.MpdErrorType;
import ru.net.arh.mpd.model.sockjs.ResponseType;
import ru.net.arh.mpd.model.sockjs.SockJsResponse;

import static ru.net.arh.mpd.web.MpdWsController.REPLY_QUEUE;

@Controller
public class MpdConnectionController {

    @Autowired
    private ConnectionService connectionService;

    /**
     * Проверка состояния соединения с mpd-сервером
     */
    @MessageMapping("/connectionState")
    @SendToUser(REPLY_QUEUE)
    @MpdErrorType(type = ResponseType.CONNECTION_STATE)
    public SockJsResponse<Boolean> connectionState() {
        return new SockJsResponse<>(ResponseType.CONNECTION_STATE, connectionService.isConnected());
    }

    /**
     * Установить / разорвать соедиение с mpd сервером
     */
    @MessageMapping({"/connectionState/change"})
    @MpdErrorType(type = ResponseType.CHANGE_CONNECTION_STATE)
    public void changeConnectionState(@RequestBody boolean newState) {
        if (newState) {
            connectionService.connect();
        } else {
            connectionService.disconnect();
        }
    }
}
