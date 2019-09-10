package ru.net.arh.mpd.services.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.net.arh.mpd.connection.ConnectionService;
import ru.net.arh.mpd.model.events.MpdEvent;
import ru.net.arh.mpd.model.events.MpdEventType;
import ru.net.arh.mpd.stomp.WsSubscribersService;

@Service
public class SongTimeService {

    @Autowired
    private ConnectionService connectionService;
    @Autowired
    private StatusService statusService;
    @Autowired
    private WsSubscribersService wsSubscribersService;
    @Autowired
    private ApplicationEventPublisher publisher;

    /**
     * При активном воспроизведении отправляет информацию о текущем времени воспроизведения
     */
    @Scheduled(fixedDelay = 500)
    public void updateSongTime() {
        if (!connectionService.isConnected()) {
            return; // нет коннекта
        }
        if (!statusService.status().getState().equals("play")) {
            return; // воспроизведение остановлено
        }
        if (!wsSubscribersService.isSubscribersFound(MpdEventType.SONG_TIME.getDestionation())) {
            return; // нет подписчиков
        }
        publisher.publishEvent(new MpdEvent<>(MpdEventType.SONG_TIME, statusService.songTime()));
    }

}
