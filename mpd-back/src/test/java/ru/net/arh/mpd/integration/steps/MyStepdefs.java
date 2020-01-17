package ru.net.arh.mpd.integration.steps;

import cucumber.api.java.Before;
import cucumber.api.java.ru.Дано;
import cucumber.api.java.ru.И;
import cucumber.api.java.ru.Когда;
import cucumber.api.java.ru.Тогда;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import ru.net.arh.mpd.integration.SpringCucumberIntegrationTest;
import ru.net.arh.mpd.integration.WsClient;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class MyStepdefs extends SpringCucumberIntegrationTest {

    private WsClient client;
    private Map<String, BlockingQueue> map = new HashMap<>();

    @Before
    public void setUp() {
        super.before();
    }


    @Дано("^клиент устанавливает соединение$")
    public void клиентУстанавливаетСоединение() throws ExecutionException, InterruptedException {
        client = super.createClient();
    }

    @И("^клиент подписывается на$")
    public void клиентПодписываетсяНа(List<String> topics) {
        topics.forEach(topic -> {
            BlockingQueue queue = new LinkedBlockingQueue();
            map.put(topic, queue);
            client.subscribe(topic, new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return null;
                }
                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    queue.add(payload);
                }
            });
        });
    }

    @Когда("^клиент отправляет пустой запрос по адресу (.*)$")
    public void клиентОтправляетПустойЗапросПоАдресуMpdConnectionState(String destination) {
        client.sendCommand(destination, null);
    }

    @Тогда("^в течение (\\d+) получает ответ из очереди (.*)$")
    public void вТечениеПолучаетОтветИзОчереди(int delay, String topic) throws InterruptedException {
        BlockingQueue queue = map.get(topic);
        Object poll = queue.poll(delay, TimeUnit.SECONDS);
        System.out.println("here");
    }

}
