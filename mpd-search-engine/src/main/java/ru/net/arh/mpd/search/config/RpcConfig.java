package ru.net.arh.mpd.search.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import ru.net.arh.mpd.search.api.SearchApi;
import ru.net.arh.mpd.search.api.SearchApiImpl;
import ru.net.arh.mpd.search.service.MyStompSessionHandler;
import ru.net.arh.mpd.search.service.TreeService;
import ru.net.arh.mpd.search.service.TreeServiceImpl;

import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;

@Configuration
@EnableScheduling
@ComponentScan("ru.net.arh.mpd")
public class RpcConfig {

    private static final int MAX_TEXT_MESSAGE_BUFFER_SIZE=20*1024*1024;

    @Bean(name="/search")
    HttpInvokerServiceExporter search(TreeService treeService) {
        HttpInvokerServiceExporter exporter = new HttpInvokerServiceExporter();
        exporter.setService(new SearchApiImpl(treeService));
        exporter.setServiceInterface(SearchApi.class);
        return exporter;
    }

}
