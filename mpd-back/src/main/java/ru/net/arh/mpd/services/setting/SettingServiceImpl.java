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
        connectionService.sendCommand(MpdCommand.of(Command.RANDOM).add(value));
    }

    @Override
    @ThrowIfNotConnected
    public void repeat(boolean value) {
        connectionService.sendCommand(MpdCommand.of(Command.REPEAT).add(value));
    }

    @Override
    @ThrowIfNotConnected
    public void single(boolean value) {
        connectionService.sendCommand(MpdCommand.of(Command.SINGLE).add(value));
    }

    @Override
    @ThrowIfNotConnected
    public void consume(boolean value) {
        connectionService.sendCommand(MpdCommand.of(Command.CONSUME).add(value));
    }
}
