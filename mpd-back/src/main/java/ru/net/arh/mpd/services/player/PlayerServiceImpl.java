package ru.net.arh.mpd.services.player;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.net.arh.mpd.aop.ThrowIfNotConnected;
import ru.net.arh.mpd.connection.ConnectionService;
import ru.net.arh.mpd.model.commands.MpdCommandBuilder;
import ru.net.arh.mpd.model.commands.MpdCommandBuilder.Command;
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
        connectionService.sendCommand(MpdCommandBuilder.of(Command.PLAY));
    }

    @Override
    @ThrowIfNotConnected
    public void pause() {
        connectionService.sendCommand(MpdCommandBuilder.of(Command.PAUSE));
    }

    @Override
    @ThrowIfNotConnected
    public void stop() {
        connectionService.sendCommand(MpdCommandBuilder.of(Command.STOP));
    }

    @Override
    @ThrowIfNotConnected
    public void prev() {
        connectionService.sendCommand(MpdCommandBuilder.of(Command.PREV));
    }

    @Override
    @ThrowIfNotConnected
    public void next() {
        connectionService.sendCommand(MpdCommandBuilder.of(Command.NEXT));
    }

    @Override
    @ThrowIfNotConnected
    public void playPos(int songPos) {
        connectionService.sendCommand(MpdCommandBuilder.of(Command.PLAY).add(songPos));
    }

    @Override
    @ThrowIfNotConnected
    public void playId(int id) {
        connectionService.sendCommand(MpdCommandBuilder.of(Command.PLAY_ID).add(id));
    }

    @Override
    @ThrowIfNotConnected
    public void seek(int songPos, int seekPos) {
        connectionService.sendCommand(MpdCommandBuilder.of(Command.SEEK).add(songPos).add(seekPos));
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