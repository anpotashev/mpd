package ru.net.arh.mpd.services.outputs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import ru.net.arh.mpd.cache.CacheNames;
import ru.net.arh.mpd.connection.ConnectionService;
import ru.net.arh.mpd.model.MpdCommand;
import ru.net.arh.mpd.model.MpdCommand.Command;
import ru.net.arh.mpd.model.MpdIdleType;
import ru.net.arh.mpd.model.events.MpdEventType;
import ru.net.arh.mpd.model.events.MpdIdleEventMethod;
import ru.net.arh.mpd.model.events.MpdIldeEventClass;
import ru.net.arh.mpd.model.outputs.MpdOutput;
import ru.net.arh.mpd.util.MpdAnswersParser;

import java.util.List;

@Slf4j
@MpdIldeEventClass
public class OutputsServiceImpl implements OutputsService {

    @Autowired
    private ConnectionService connectionService;

    @Override
    @Cacheable(cacheNames = {CacheNames.Constants.OUTPUT})
    @MpdIdleEventMethod(types = {MpdIdleType.OUTPUT}, eventType = MpdEventType.OUTPUT)
    public List<MpdOutput> outputs() {
        return MpdAnswersParser.parseAll(
                MpdOutput.class,
                connectionService.sendCommand(new MpdCommand(Command.OUTPUTS))
        );
    }

    @Override
    public void save(MpdOutput output) {
        MpdCommand command = new MpdCommand(output.isEnabled()
                                                    ? Command.ENABLE_OUTPUT
                                                    : Command.DISABLE_OUTPUT
        );
        command.addParam(output.getId() + "");
        connectionService.sendCommand(command);
    }

}
