package ru.net.arh.mpd.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import ru.net.arh.mpd.model.MpdErrorType;
import ru.net.arh.mpd.model.player.PlayerCmdRq;
import ru.net.arh.mpd.model.player.SeekRequest;
import ru.net.arh.mpd.model.sockjs.ResponseType;
import ru.net.arh.mpd.services.player.PlayerService;

@Controller
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
    public void player(PlayerCmdRq playerCommand) {
        playerService.doCommand(playerCommand.getCmd());
    }

    @MessageMapping("/player/seek")
    @MpdErrorType(type = ResponseType.PLAYER)
    public void seek(SeekRequest seekRequest) {
        playerService.seek(seekRequest.getSongPos(), seekRequest.getSeekPos());
    }
}
