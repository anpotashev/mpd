package ru.net.arh.mpd.web.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import ru.net.arh.mpd.connection.ConnectionService;

@Controller
public class ConnectionEndPoint {
    @Autowired
    private ConnectionService connectionService;
    @MessageMapping("/connectionState")
    @SendToUser
    public boolean isConnected() {
        return connectionService.isConnected();
    }
}
