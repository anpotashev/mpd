package ru.net.arh.mpd.integration;

import org.springframework.messaging.converter.ByteArrayMessageConverter;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.testcontainers.shaded.org.apache.commons.lang.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.SECONDS;

public class WsClient {

    private int port;
    private String host;
    private boolean connected;
    private WebSocketStompClient client;
    private StompSession stompSession;

    public WsClient(String host, int port) {
        this.port = port;
        this.host = host;
        client = new WebSocketStompClient(
                new SockJsClient(createTransportClient())
        );
        client.setMessageConverter(new MappingJackson2MessageConverter());
    }

    public void connect() throws ExecutionException, InterruptedException {
        stompSession = client.connect("ws://" + host + ":" + port + "/stomp", new StompSessionHandlerAdapter() {
        }).get();
        connected = true;
    }

    private List<Transport> createTransportClient() {
        return Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient()));
    }

    public void sendCommand(String destination, Object payload) {
        stompSession.send(destination, payload);
    }

    public void subscribe(String s, StompFrameHandler handler) {
        stompSession.subscribe(s, handler);
    }
}
