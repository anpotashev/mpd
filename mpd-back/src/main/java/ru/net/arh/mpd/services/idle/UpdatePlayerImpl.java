package ru.net.arh.mpd.services.idle;

import org.springframework.stereotype.Service;
import ru.net.arh.mpd.model.events.MpdEvent;
import ru.net.arh.mpd.model.events.MpdEventType;

@Service("updatePlayerService")
public class UpdatePlayerImpl implements UpdateService {

    @Override
    public MpdEvent createUpdateEvent() {
        return new MpdEvent(MpdEventType.PLAYER_CHANGED, null);
    }
}
