package ru.net.arh.mpd.services.status;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import ru.net.arh.mpd.cache.CacheNames;
import ru.net.arh.mpd.connection.ConnectionService;
import ru.net.arh.mpd.model.MpdCommand;
import ru.net.arh.mpd.model.MpdIdleType;
import ru.net.arh.mpd.model.events.MpdEventType;
import ru.net.arh.mpd.model.events.MpdIdleEventMethod;
import ru.net.arh.mpd.model.events.MpdIldeEventClass;
import ru.net.arh.mpd.model.status.MpdShortStatus;
import ru.net.arh.mpd.model.status.MpdStatus;
import ru.net.arh.mpd.util.MpdAnswersParser;

@Slf4j
@MpdIldeEventClass
public class StatusServiceImpl implements StatusService {

    @Autowired
    private ConnectionService connectionService;

    @Override
    @Cacheable(cacheNames = CacheNames.Constants.STATUS)
    @MpdIdleEventMethod(types = {MpdIdleType.STATUS, MpdIdleType.PLAYLIST, MpdIdleType.PLAYER, MpdIdleType.OPTIONS}, eventType = MpdEventType.STATUS_CHANGED)
    public MpdStatus status() {
        return MpdAnswersParser.parse(
                MpdStatus.class,
                connectionService.sendCommand(
                        new MpdCommand(MpdCommand.Command.STATUS)
                )
        );
    }

    @Override
    public MpdShortStatus songTime() {
        MpdStatus status = MpdAnswersParser.parse(
                MpdStatus.class,
                connectionService.sendCommand(
                        new MpdCommand(MpdCommand.Command.STATUS)
                )
        );
        MpdShortStatus result = new MpdShortStatus();
        result.setSongTime(status.getTime());
        result.setSongPos(status.getSong());
        result.setPlaying(status.getState().equals("play"));
        return result;
    }
}
