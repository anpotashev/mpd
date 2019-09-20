package ru.net.arh.mpd.services.player;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.net.arh.mpd.aop.ThrowIfNotConnected;
import ru.net.arh.mpd.connection.ConnectionService;
import ru.net.arh.mpd.model.MpdCommand.Command;
import ru.net.arh.mpd.model.player.PlayerCommand;

@Slf4j
@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private ConnectionService connectionService;

    @Autowired
    private PlayerService playerService;

    @Override
    @ThrowIfNotConnected
    public void play() {
        connectionService.sendCommand(Command.PLAY.build());
    }

    @Override
    @ThrowIfNotConnected
    public void pause() {
        connectionService.sendCommand(Command.PAUSE.build());
    }

    @Override
    @ThrowIfNotConnected
    public void stop() {
        connectionService.sendCommand(Command.STOP.build());
    }

    @Override
    @ThrowIfNotConnected
    public void prev() {
        connectionService.sendCommand(Command.PREV.build());
    }

    @Override
    @ThrowIfNotConnected
    public void next() {
        connectionService.sendCommand(Command.NEXT.build());
    }

    @Override
    @ThrowIfNotConnected
    public void playPos(int songPos) {
        connectionService.sendCommand(Command.PLAY.build(songPos + ""));
    }

    @Override
    @ThrowIfNotConnected
    public void playId(int id) {
        connectionService.sendCommand(Command.PLAY_ID.build(id + ""));
    }

    @Override
    @ThrowIfNotConnected
    public void seek(int songPos, int seekPos) {
        connectionService.sendCommand(Command.SEEK.build(songPos+"", seekPos+""));
    }

    @Override
    public void doCommand(PlayerCommand playerCommand) {
        switch (playerCommand) {
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