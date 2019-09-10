package ru.net.arh.mpd.connection.idle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.net.arh.mpd.connection.ConnectionService;
import ru.net.arh.mpd.events.EventsService;
import ru.net.arh.mpd.model.events.MpdEvent;
import ru.net.arh.mpd.model.exception.MpdException;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class IdleService {

    private static final String CHANGED = "changed: ";
    @Autowired
    private ConnectionService connectionService;
    @Autowired
    private EventsService EventsService;
    private Thread t;

    private Runnable runnable = () -> {
        while (true) {
            getIdleSystems(connectionService.sendIdleCommand())
                    .stream()
                    .forEach(s -> EventsService.onIdle(s));
        }
    };

    @EventListener(condition = "#event.type == T(ru.net.arh.mpd.model.events.MpdEventType).CONNECTION_STATE_CHANGED")
    private void changedEvent(MpdEvent event) {
        if ((Boolean) event.getBody()) {
            startListeningIdle();
        } else {
            stopListeningIdle();
        }
    }

    private void startListeningIdle() {
        t = new Thread(runnable);
        t.start();
    }

    private void stopListeningIdle() {
        t.interrupt();
    }

    private List<String> getIdleSystems(List<String> list) {
        if (list == null || list.isEmpty()) {
            log.error("got empty message");
        }
        if (list.get(list.size() - 1).startsWith("ACK")) {
            String error = "got error " + list.get(list.size() - 1);
            log.error(error);
            throw new MpdException(error);
        }
        return list.stream()
                .filter(s -> s.startsWith(CHANGED))
                .map(s -> s.substring(CHANGED.length()))
                .collect(toList());
    }

}
