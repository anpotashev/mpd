package ru.net.arh.mpd.connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.net.arh.mpd.model.MpdCommand;
import ru.net.arh.mpd.model.MpdCommand.Command;

/**
 * Периодеская отправка команды ping, чтобы сервер не разорвал соединение.
 */
@PropertySources({
        @PropertySource("classpath:/config/application.yaml")
        ,
        @PropertySource(value = "file:${MPD_CONFIG}/mpd.properties", ignoreResourceNotFound = true)
})
@Service
public class PingService {

    @Autowired
    private ConnectionService connectionService;

    @Scheduled(fixedDelayString = "${mpdserver.pinginterval:${mpdserver.defaultpinginterval}}")
    private void ping() {
        if (connectionService.isConnected()) {
            connectionService.sendCommand(MpdCommand.of(Command.PING));
        }
    }
}
