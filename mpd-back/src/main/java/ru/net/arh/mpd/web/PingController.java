package ru.net.arh.mpd.web;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class PingController {

    @MessageMapping("/ping")
    @SendTo("/topics/ping")
    public String ping() {
        return "pong";
    }
}
