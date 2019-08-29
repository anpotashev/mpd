package ru.net.arh.mpd.connection.idle;

import static java.util.stream.Collectors.toList;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.net.arh.mpd.connection.ConnectionService;
import ru.net.arh.mpd.events.EventsService;
import ru.net.arh.mpd.model.events.MpdEvent;
import ru.net.arh.mpd.model.events.MpdEventType;
import ru.net.arh.mpd.util.ExceptionUtil;

@Slf4j
@Service
public class IdleServiceImpl implements IdleService {

  @Autowired
  private ConnectionService connectionService;

  @Autowired
  private EventsService EventsService;

  private static final String CHANGED = "changed: ";
  private Thread t;

  private Runnable runnable = () -> {
    while (true) {
      getIdleSystems(connectionService.sendIdleCommand())
          .stream()
          .forEach(s -> EventsService.onIdle(s));
    }
  };

  @EventListener
  private void changedEvent(MpdEvent event) {
    if(event.getType() == MpdEventType.CONNECTION_STATE_CHANGED) {
      if ((Boolean) event.getBody()) {
        startListeningIdle();
      } else {
        stopListeningIdle();
      }
    }
  }

  @Override
  //@EventListener(condition = "#event.type == T(ru.net.arh.events.MpdEventType).CONNECTION_STATE_CHANGED && #event.body")
  //@EventListener(condition = "#event.type == T(ru.net.arh.events.MpdEventType).CONNECTION_STATE_CHANGED")
  //@EventListener
  public void startListeningIdle() {
    t = new Thread(runnable);
    t.start();
  }

  @Override
  //@EventListener(condition = "#event.type == T(ru.net.arh.events.MpdEventType).CONNECTION_STATE_CHANGED && !#event.body")
  public void stopListeningIdle() {
    t.interrupt();
  }

  private List<String> getIdleSystems(List<String> list) {
    if (list == null || list.isEmpty()) {
      ExceptionUtil.logAndGetException("got empty message", log);
    }
    if (list.get(list.size() - 1).startsWith("ACK")) {
      ExceptionUtil.logAndGetException("got error " + list.get(list.size() - 1), log);
    }
    return list.stream()
        .filter(s -> s.startsWith(CHANGED))
        .map(s -> s.substring(CHANGED.length()))
        .collect(toList());
  }

}