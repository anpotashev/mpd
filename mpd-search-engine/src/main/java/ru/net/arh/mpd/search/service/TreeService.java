package ru.net.arh.mpd.search.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import javax.annotation.PostConstruct;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


@Service
public class TreeService {
    final static CountDownLatch messageLatch = new CountDownLatch(1);
    private String URL = "ws://localhost:8080/javaclient";
    private static final int MAX_TEXT_MESSAGE_BUFFER_SIZE=20*1024*1024;
    @Autowired
    private MyStompSessionHandler sessionHandler;
    WebSocketStompClient stompClient;

    @PostConstruct
    public void init() throws IOException, DeploymentException, InterruptedException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.setDefaultMaxTextMessageBufferSize(MAX_TEXT_MESSAGE_BUFFER_SIZE);
        WebSocketClient client = new StandardWebSocketClient(container);

//        List<Transport> transports = new ArrayList<>(1);
//        transports.add(new WebSocketTransport( new StandardWebSocketClient()) );
//        WebSocketClient transport = new SockJsClient(transports);
//        WebSocketStompClient stompClient = new WebSocketStompClient(transport);
//        WebSocketClient client = new StandardWebSocketClient();
        stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        stompClient.setInboundMessageSizeLimit(Integer.MAX_VALUE);
        stompClient.connect(URL, sessionHandler);

    }
}
