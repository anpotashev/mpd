package ru.net.arh.mpd.events;

import ru.net.arh.mpd.model.MpdIdleType;

public interface IdleEventService {
    void processIdleEvent(MpdIdleType type);
}
