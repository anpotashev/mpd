package ru.net.arh.mpd.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import ru.net.arh.mpd.model.MpdErrorType;
import ru.net.arh.mpd.model.sockjs.ResponseType;
import ru.net.arh.mpd.model.sockjs.SockJsResponse;
import ru.net.arh.mpd.model.status.MpdStatus;
import ru.net.arh.mpd.services.setting.SettingService;
import ru.net.arh.mpd.services.status.StatusService;

import static ru.net.arh.mpd.web.MpdWsController.REPLY_QUEUE;

@Controller
public class MpdStatusController {

    @Autowired
    private StatusService statusService;
    @Autowired
    private SettingService settingService;

    /**
     * Запрос текущего состояния (много параметров)
     */
    @MessageMapping("/status")
    @SendToUser(REPLY_QUEUE)
    @MpdErrorType(type = ResponseType.STATUS)
    public SockJsResponse<MpdStatus> status() {
        return new SockJsResponse<>(ResponseType.STATUS, statusService.status());
    }

    @MessageMapping("/status/repeat")
    public void repeat(boolean newState) {
        settingService.repeat(newState);
    }

    @MessageMapping("/status/consume")
    public void consume(boolean newState) {
        settingService.consume(newState);
    }

    @MessageMapping("/status/random")
    public void random(boolean newState) {
        settingService.random(newState);
    }

    @MessageMapping("/status/single")
    public void single(boolean newState) {
        settingService.single(newState);
    }
}
