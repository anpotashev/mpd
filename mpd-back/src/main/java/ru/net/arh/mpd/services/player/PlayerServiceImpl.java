package ru.net.arh.mpd.services.player;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.net.arh.mpd.aop.ThrowIfNotConnected;
import ru.net.arh.mpd.connection.ConnectionService;
import ru.net.arh.mpd.model.MpdCommand;
import ru.net.arh.mpd.model.MpdCommand.Command;

@Slf4j
@Service
public class PlayerServiceImpl implements PlayerService {


    private final ConnectionService connectionService;

    @Autowired
    public PlayerServiceImpl(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @Override
    @ThrowIfNotConnected
    public void play() {
        connectionService.sendCommand(new MpdCommand(Command.PLAY));
    }

    @Override
    @ThrowIfNotConnected
    public void pause() {
        connectionService.sendCommand(new MpdCommand(Command.PAUSE));
    }

    @Override
    @ThrowIfNotConnected
    public void stop() {
        connectionService.sendCommand(new MpdCommand(Command.STOP));
    }

    @Override
    @ThrowIfNotConnected
    public void prev() {
        connectionService.sendCommand(new MpdCommand(Command.PREV));
    }

    @Override
    @ThrowIfNotConnected
    public void next() {
        connectionService.sendCommand(new MpdCommand(Command.NEXT));
    }

    @Override
    @ThrowIfNotConnected
    public void playPos(int songPos) {
        MpdCommand command = new MpdCommand(Command.PLAY);
        command.addParam(songPos + "");
        connectionService.sendCommand(command);
    }

    @Override
    @ThrowIfNotConnected
    public void playId(int id) {
        MpdCommand command = new MpdCommand(Command.PLAY_ID);
        command.addParam(id + "");
        connectionService.sendCommand(command);
    }

    @Override
    @ThrowIfNotConnected
    public void seek(int seekPos, int songPos) {
        MpdCommand command = new MpdCommand(Command.SEEK);
        command.addParam(songPos + "");
        command.addParam(seekPos + "");
        connectionService.sendCommand(command);
    }
}