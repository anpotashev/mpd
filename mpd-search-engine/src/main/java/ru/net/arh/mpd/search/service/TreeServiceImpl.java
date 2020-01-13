package ru.net.arh.mpd.search.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.AnalyzeRequest;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import ru.net.arh.mpd.search.model.StompEvent;
import ru.net.arh.mpd.search.model.TreeItem;
import ru.net.arh.mpd.search.util.TreeItemUtil;

import javax.annotation.PostConstruct;
import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

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

    private static final int MAX_TEXT_MESSAGE_BUFFER_SIZE = 20 * 1024 * 1024;

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
    @Value("${elasticsearch.index}")
    private String eSindex;

    private WebSocketStompClient stompClient;

    @Getter
    private List<TreeItem> items;

    @PostConstruct
    public void init1() throws IOException, URISyntaxException {
        dropIndexIfExists();
        createIndexWithMapping();
    }

    private void createIndexWithMapping() throws IOException, URISyntaxException {
        CreateIndexRequest request = new CreateIndexRequest(eSindex);
        String setting = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("es-config/analysis.json").toURI())));
        request.settings(setting, XContentType.JSON);
        String mapping = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("es-config/mapping.json").toURI())));
        request.mapping(mapping, XContentType.JSON);
        client.indices().create(request, RequestOptions.DEFAULT);
    }

    private void dropIndexIfExists() throws IOException {
        if (client.indices().exists(new GetIndexRequest(eSindex), RequestOptions.DEFAULT)) {
            client.indices().delete(new DeleteIndexRequest(eSindex), RequestOptions.DEFAULT);
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
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
        }, new Date(System.currentTimeMillis() + 5000L));
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
        } catch (IOException e) {
        }
    }


    private void add(List<TreeItem> items) throws IOException {
        BulkRequest request = new BulkRequest();
        items.forEach(treeItem -> request.add(new IndexRequest(eSindex).id(treeItem.getPath()).source(toJson(treeItem), XContentType.JSON)));
        client.bulk(request, RequestOptions.DEFAULT);
    }

    private void delete() throws IOException {
        DeleteByQueryRequest request = new DeleteByQueryRequest(eSindex);
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
