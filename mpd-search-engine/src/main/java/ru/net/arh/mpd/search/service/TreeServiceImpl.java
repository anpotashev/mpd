package ru.net.arh.mpd.search.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
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

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Импелментация сервиса, в основу которого положено взаимодействие с приложением mpd-back по ws протоколу.
 */
@Service
@PropertySources({
        @PropertySource("classpath:application.yaml")
        ,
        @PropertySource(value = "file:./custom/mpd-search.properties", ignoreResourceNotFound = true)
})
public class TreeServiceImpl implements TreeService {

    private static final int MAX_TEXT_MESSAGE_BUFFER_SIZE=20*1024*1024;

    @Value("${mpdback.address:${mpdback.defaultAddress}}")
    private String URL;
    @Autowired
    private MyStompSessionHandler sessionHandler;
    @Autowired
    private TaskScheduler scheduler;
    @Autowired
    private RestHighLevelClient client;
    @Autowired
    private ObjectMapper objectMapper;

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
        try {
            try {
                delete();
            } catch (ElasticsearchStatusException e) {

            }
            add(this.items);
        } catch (IOException e) {}
    }


    private void add(List<TreeItem> items) throws IOException {
        BulkRequest request = new BulkRequest();
        items.forEach(treeItem -> request.add(new IndexRequest("mpd").id(treeItem.getPath()).source(toJson(treeItem), XContentType.JSON)));
        client.bulk(request, RequestOptions.DEFAULT);
    }

    private void delete() throws IOException {
        DeleteByQueryRequest request = new DeleteByQueryRequest("mpd");
        request.setQuery(new MatchAllQueryBuilder());
        client.deleteByQuery(request, RequestOptions.DEFAULT);
    }

    private String toJson(TreeItem treeItem) {
        try {
            return objectMapper.writeValueAsString(treeItem);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
}
