package ru.net.arh.mpd.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import ru.net.arh.mpd.model.MpdErrorType;
import ru.net.arh.mpd.model.player.PlayerCommand;
import ru.net.arh.mpd.model.sockjs.ResponseType;
import ru.net.arh.mpd.services.player.PlayerService;
import ru.net.arh.mpd.validation.MapKeys;

import java.util.Map;

@Controller
@Validated
public class MpdPlayerController {

    @Autowired
    private PlayerService playerService;

    /**
     * Воспроизвести трек по его id
     */
    @MessageMapping("/player/playid")
    @MpdErrorType(type = ResponseType.PLAYER)
    public void playId(Integer id) {
        playerService.playId(id);
    }

    /**
     * Воспроизвести трек по его позиции
     */
    @MessageMapping("/player/playpos")
    @MpdErrorType(type = ResponseType.PLAYER)
    public void playPos(Integer id) {
        playerService.playPos(id);
    }

    /**
     * Управление воспроизведением
     */
    @MessageMapping("/player")
    @MpdErrorType(type = ResponseType.PLAYER)
    public void player(@MapKeys(keys = {"cmd"}) Map<String, String> map) {
        playerService.doCommand(PlayerCommand.valueOf(map.get("cmd")));
    }

    @MessageMapping("/player/seek")
    @MpdErrorType(type = ResponseType.PLAYER)
    public void seek(@MapKeys(keys = {"songPos", "seekPos"}) Map<String, Integer> map) {
        playerService.seek(map.get("songPos"), map.get("seekPos"));
    }
}
