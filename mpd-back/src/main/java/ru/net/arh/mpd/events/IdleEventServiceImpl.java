package ru.net.arh.mpd.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.net.arh.mpd.model.MpdIdleType;
import ru.net.arh.mpd.services.idle.UpdateService;

import java.util.Arrays;

@Service
@Slf4j
public class IdleEventServiceImpl {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    @Qualifier("updatePlayerService")
    private UpdateService playerService;
    @Autowired
    @Qualifier("updatePlaylistService")
    private UpdateService playlistService;

    @EventListener(condition = "#type == T(ru.net.arh.mpd.model.MpdIdleType).PLAYER")
    private void onPlayerEvent(MpdIdleType type) {
        process(playerService, playlistService);
    }

    @EventListener(condition = "#type == T(ru.net.arh.mpd.model.MpdIdleType).PLAYLIST")
    private void onPlaylistEvent(MpdIdleType type) {
        process(playlistService);
    }

    private void process(UpdateService...services) {
        Arrays.asList(services).forEach(updateService -> publisher.publishEvent(updateService.createUpdateEvent()));
    }
}
