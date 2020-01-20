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
import ru.net.arh.mpd.model.sockjs.SockJsResponse;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MyStepdefs extends SpringCucumberIntegrationTest {

    private WsClient client;
    private Map<String, BlockingQueue> map = new HashMap<>();
    private Boolean connectionState;
    private Map<String, Object> savedValues = new HashMap<>();

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
                    return SockJSResponse.class;
                }
                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    if (!(payload instanceof SockJSResponse)) return;
                    queue.add(payload);
                }
            });
        });
    }

    @Когда("^клиент отправляет пустой запрос по адресу (.*)$")
    public void клиентОтправляетПустойЗапросПоАдресуMpdConnectionState(String destination) {
        client.sendCommand(destination, null);
    }

    @Тогда("^в течение (\\d+) получает ответ из очереди (.*) и сохраняет его значение по ключу \"(.*)\"$")
    public void вТечениеПолучаетОтветИзОчереди(int delay, String topic, String key) throws InterruptedException {
        BlockingQueue queue = map.get(topic);
        SockJSResponse result = (SockJSResponse)queue.poll(delay, TimeUnit.SECONDS);
        assertNotNull(result);
        savedValues.put(key, result.getPayload());
    }

    @И("^\"(.*)\" - (true|false)$")
    public void checkBoolean(String key, boolean value) {
        Boolean b = (Boolean) savedValues.get(key);
        assertEquals(b, value);
    }
}
