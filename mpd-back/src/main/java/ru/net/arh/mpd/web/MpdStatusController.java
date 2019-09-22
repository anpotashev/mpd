package ru.net.arh.mpd.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import ru.net.arh.mpd.model.MpdErrorType;
import ru.net.arh.mpd.model.sockjs.ResponseType;
import ru.net.arh.mpd.model.sockjs.SockJsResponse;
import ru.net.arh.mpd.model.status.MpdShortStatus;
import ru.net.arh.mpd.model.status.MpdSongTime;
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

    @MessageMapping("/status/short")
    @SendToUser(REPLY_QUEUE)
    @MpdErrorType(type = ResponseType.SONG_TIME)
    public SockJsResponse<MpdShortStatus> shortStatus() {
        return new SockJsResponse<>(ResponseType.SONG_TIME, statusService.songTime());
    }

    @MessageMapping("/status/repeat")
    @MpdErrorType(type = ResponseType.REPEAT)
    public void repeat(boolean newState) {
        settingService.repeat(newState);
    }

    @MessageMapping("/status/consume")
    @MpdErrorType(type = ResponseType.CONSUME)
    public void consume(boolean newState) {
        settingService.consume(newState);
    }

    @MessageMapping("/status/random")
    @MpdErrorType(type = ResponseType.RANDOM)
    public void random(boolean newState) {
        settingService.random(newState);
    }

    @MessageMapping("/status/single")
    @MpdErrorType(type = ResponseType.SINGLE)
    public void single(boolean newState) {
        settingService.single(newState);
    }
}
