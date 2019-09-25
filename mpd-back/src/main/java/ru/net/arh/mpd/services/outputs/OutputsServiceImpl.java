package ru.net.arh.mpd.services.outputs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.net.arh.mpd.aop.ThrowIfNotConnected;
import ru.net.arh.mpd.cache.CacheNames;
import ru.net.arh.mpd.connection.ConnectionService;
import ru.net.arh.mpd.model.MpdIdleType;
import ru.net.arh.mpd.model.commands.MpdCommand;
import ru.net.arh.mpd.model.commands.MpdCommandBuilder;
import ru.net.arh.mpd.model.commands.MpdCommandBuilder.Command;
import ru.net.arh.mpd.model.events.MpdEventType;
import ru.net.arh.mpd.model.events.MpdIdleEventMethod;
import ru.net.arh.mpd.model.outputs.MpdOutput;
import ru.net.arh.mpd.util.MpdAnswersParser;

import java.util.List;

@Slf4j
@Service
public class OutputsServiceImpl implements OutputsService {

    @Autowired
    private ConnectionService connectionService;

    @Override
    @ThrowIfNotConnected
    @Cacheable(cacheNames = {CacheNames.Constants.OUTPUT})
    @MpdIdleEventMethod(types = {MpdIdleType.OUTPUT}, eventType = MpdEventType.OUTPUT)
    public List<MpdOutput> outputs() {
        return MpdAnswersParser.parseAll(
                MpdOutput.class,
                connectionService.sendCommand(MpdCommandBuilder.of(Command.OUTPUTS))
        );
    }

    @Override
    @ThrowIfNotConnected
    public void save(MpdOutput output) {
        MpdCommand command = output.isEnabled()
                ? MpdCommandBuilder.of(Command.ENABLE_OUTPUT).add(output.getId())
                : MpdCommandBuilder.of(Command.DISABLE_OUTPUT).add(output.getId() + "");
        connectionService.sendCommand(command);
    }

}
