package ru.net.arh.mpd.web.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import ru.net.arh.mpd.model.player.PlayerCommand;
import ru.net.arh.mpd.services.player.PlayerService;

@Controller
public class PlayerEndPoint {

    @Autowired
    private PlayerService playerService;

    @MessageMapping("/player")
    public void player(String playerCommand) {
        switch (PlayerCommand.valueOf(playerCommand)) {
            case PREV:
                playerService.prev();
                break;
            case PLAY:
                playerService.play();
                break;
            case PAUSE:
                playerService.pause();
                break;
            case STOP:
                playerService.stop();
                break;
            case NEXT:
                playerService.next();
                break;
        }
    }

}
