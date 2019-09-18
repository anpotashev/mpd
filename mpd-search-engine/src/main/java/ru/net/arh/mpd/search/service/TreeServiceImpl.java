package ru.net.arh.mpd.search.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import ru.net.arh.mpd.search.model.StompEvent;
import ru.net.arh.mpd.search.model.TreeItem;
import ru.net.arh.mpd.search.util.TreeItemUtil;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class TreeServiceImpl implements TreeService {

    private static final int MAX_TEXT_MESSAGE_BUFFER_SIZE=20*1024*1024;

    @Value("${ws.address:ws://localhost:8080/javaclient}")
    private String URL;
    @Autowired
    private MyStompSessionHandler sessionHandler;
    @Autowired
    private TaskScheduler scheduler;

    private WebSocketStompClient stompClient;

    @Getter
    private List<TreeItem> items;

    @EventListener(ApplicationReadyEvent.class)
    public void init() throws IOException, DeploymentException, InterruptedException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.setDefaultMaxTextMessageBufferSize(MAX_TEXT_MESSAGE_BUFFER_SIZE);
        WebSocketClient client = new StandardWebSocketClient(container);
        stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        stompClient.setInboundMessageSizeLimit(Integer.MAX_VALUE);
        stompClient.connect(URL, sessionHandler);
    }

    @EventListener(condition = "#event.type == T(ru.net.arh.mpd.search.model.EventType).SOCKS_CONNECTED")
    private void onSocksConnect(StompEvent event) {

    }

    @EventListener(condition = "#event.type == T(ru.net.arh.mpd.search.model.EventType).SOCKS_DISCONNECTED")
    private void onSocksDiconnect(StompEvent event) {
        this.items = null;
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                stompClient.connect(URL, sessionHandler);
            }
        }, new Date( System.currentTimeMillis() + 5000L));
    }

    @EventListener(condition = "#event.type == T(ru.net.arh.mpd.search.model.EventType).CONNECTION_STATE")
    private void onConnectionStateChanged(StompEvent event) {
        if (!(Boolean) event.getBody()) {
            this.items = null;
        }
    }

    @EventListener(condition = "#event.type == T(ru.net.arh.mpd.search.model.EventType).FULL_TREE")
    private void onFullTreeGet(StompEvent event) {
        this.items = TreeItemUtil.setPathConvertToListAndRemoveDirs((TreeItem) event.getBody());
    }

}
