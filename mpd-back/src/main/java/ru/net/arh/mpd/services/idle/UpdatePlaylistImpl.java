package ru.net.arh.mpd.services.idle;

import org.springframework.stereotype.Service;
import ru.net.arh.mpd.model.events.MpdEvent;
import ru.net.arh.mpd.model.events.MpdEventType;

@Service("updatePlaylistService")
public class UpdatePlaylistImpl implements UpdateService {

    @Override
    public MpdEvent createUpdateEvent() {
        return new MpdEvent(MpdEventType.PLAYLIST_CHANGED, null);
    }
}
