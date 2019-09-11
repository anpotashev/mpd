package ru.net.arh.mpd.services.setting;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.net.arh.mpd.aop.ThrowIfNotConnected;
import ru.net.arh.mpd.connection.ConnectionService;
import ru.net.arh.mpd.model.MpdCommand;
import ru.net.arh.mpd.model.MpdCommand.Command;

@Slf4j
@Service
public class SettingServiceImpl implements SettingService {

    @Autowired
    private ConnectionService connectionService;

    @Override
    @ThrowIfNotConnected
    public void random(boolean value) {
        MpdCommand command = new MpdCommand(Command.RANDOM);
        command.addParam(value);
        connectionService.sendCommand(command);
    }

    @Override
    @ThrowIfNotConnected
    public void repeat(boolean value) {
        MpdCommand command = new MpdCommand(Command.REPEAT);
        command.addParam(value);
        connectionService.sendCommand(command);
    }

    @Override
    @ThrowIfNotConnected
    public void single(boolean value) {
        MpdCommand command = new MpdCommand(Command.SINGLE);
        command.addParam(value);
        connectionService.sendCommand(command);
    }

    @Override
    @ThrowIfNotConnected
    public void consume(boolean value) {
        MpdCommand command = new MpdCommand(Command.CONSUME);
        command.addParam(value);
        connectionService.sendCommand(command);
    }
}
