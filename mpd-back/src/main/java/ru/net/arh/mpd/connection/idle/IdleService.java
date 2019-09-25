package ru.net.arh.mpd.connection.idle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.net.arh.mpd.connection.ConnectionService;
import ru.net.arh.mpd.events.EventsService;
import ru.net.arh.mpd.model.events.MpdEvent;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class IdleService {

    private static final String CHANGED = "changed: ";
    @Autowired
    private ConnectionService connectionService;
    @Autowired
    private EventsService eventsService;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @EventListener(condition = "#event.type == T(ru.net.arh.mpd.model.events.MpdEventType).CONNECTION_STATE_CHANGED")
    private void changedEvent(MpdEvent event) {
        if ((Boolean) event.getBody()) {
            executor.execute(new IdleTask());
        }
    }

    private List<String> getIdleSystems(List<String> list) {
        return list.stream()
                .filter(s -> s.startsWith(CHANGED))
                .map(s -> s.substring(CHANGED.length()))
                .collect(toList());
    }

    class IdleTask implements Runnable {
        @Override
        public void run() {
            while (connectionService.isConnected()) {
                IdleService.this.getIdleSystems(connectionService.sendIdleCommand())
                        .stream()
                        .forEach(s -> eventsService.onIdle(s));
            }
        }
    }
}
