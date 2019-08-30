package ru.net.arh.mpd.services.status;

import ru.net.arh.mpd.model.status.MpdShortStatus;
import ru.net.arh.mpd.model.status.MpdStatus;

public interface StatusService {
    MpdStatus status();

    MpdShortStatus songTime();
}
