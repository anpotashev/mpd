package ru.net.arh.mpd.integration.steps;

import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.ru.Дано;
import cucumber.api.java.ru.И;
import cucumber.api.java.ru.Когда;
import cucumber.api.java.ru.Тогда;
import org.springframework.test.util.ReflectionTestUtils;
import ru.net.arh.mpd.integration.SpringCucumberIntegrationTest;
import ru.net.arh.mpd.integration.WsClient;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
            client.subscribe(topic, (payload -> queue.add(payload)));
        });
    }

    @Когда("^клиент отправляет пустой запрос по адресу (.*)$")
    public void клиентОтправляетПустойЗапросПоАдресуMpdConnectionState(String destination) {
        client.sendCommand(destination, null);
    }

    @Тогда("^в течение (\\d+) секунд получает ответ из очереди (.*) и сохраняет его значение по ключу \"(.*)\"$")
    public void вТечениеСекундПолучаетОтветИзОчереди(int delay, String topic, String key) throws InterruptedException {
        BlockingQueue queue = map.get(topic);
        Object object = queue.poll(delay, TimeUnit.SECONDS);
        assertNotNull(object);
        if (object instanceof SockJSResponse) {
            SockJSResponse result = (SockJSResponse) object;
            assertNotNull(result);
            savedValues.put(key, result.getPayload());
            return;
        }
        savedValues.put(key, object);
    }

    @И("^\"(.*)\" - (true|false)$")
    public void checkBoolean(String key, boolean value) {
        Boolean b = (Boolean) savedValues.get(key);
        assertEquals(b, value);
    }

    @Когда("^клиент отправляет \"(.*)\" по адресу (.*)$")
    public void клиентОтправляетПоАдресу(String command, String address) {
        client.sendCommand(address, command);
    }

    @И("^\"([^\"]*)\" содержит поля:$")
    public void содержитПоля(String key, DataTable dataTable) throws Throwable {
        Object o = savedValues.get(key);
        assertTrue(checkValues(dataTable, o));
    }

    @И("^\"([^\"]*)\" содержит элемент с полями:$")
    public void содержитСтрокуСПолями(String key, DataTable dataTable) throws Throwable {
        Object[] arr = (Object[]) savedValues.get(key);
        List<Map<String, Object>> values = dataTable.asMaps(String.class, Object.class);
        assertTrue(
                Arrays.stream(arr)
                        .map(o -> checkValues(dataTable, o))
                        .reduce(Boolean::logicalOr)
                        .orElse(Boolean.FALSE));
    }

    private boolean checkValues(DataTable dataTable, Object object) {
        return dataTable.asMaps(String.class, Object.class).stream()
                .map(s -> {
                    Object fieldValue = ReflectionTestUtils.getField(object, (String) s.get("поле"));
                    fieldValue = fieldValue == null ? "null" : fieldValue.toString();
                    return s.get("значение").equals(fieldValue);
                    })
                .reduce(Boolean::logicalAnd)
                .orElse(Boolean.FALSE);
    }

    @Когда("^клиент отправляет по адресу (.*) объект с полями:$")
    public void клиентОтправляетПоАдресуОбъектСПолями(String destination, DataTable dataTable) {
        Map<String, String> map = new HashMap<>();
        for (Map<String, String> asMap : dataTable.asMaps(String.class, String.class)) {
            map.put(asMap.get("поле"), asMap.get("значение"));
        }
        client.sendCommand(destination, map);
    }


    @Before
    public void before() {
        connectionService.disconnect();
    }

    @И("^У элемента \"([^\"]*)\" в поле \"([^\"]*)\" есть элемент с полями:$")
    public void уЭлементаВПолеЕстьЭлементСПолями(String key, String fieldName, DataTable dataTable) throws Throwable {
        Object obj = savedValues.get(key);

        Object field = ReflectionTestUtils.getField(obj, fieldName);
        Stream<Object> stream = (field instanceof Object[])
                ? Arrays.stream((Object[]) field)
                : ((Collection) field).stream();
        assertTrue(stream
                .map(o -> checkValues(dataTable, o))
                .reduce(Boolean::logicalOr)
                .orElse(Boolean.FALSE));
    }

    @И("^У элемента \"([^\"]*)\" в поле \"([^\"]*)\" последний элемент с полями:$")
    public void уЭлементаВПолеПоследнийЭлементСПолями(String key, String fieldName, DataTable dataTable) throws Throwable {
        Object obj = savedValues.get(key);
        Object field = ReflectionTestUtils.getField(obj, fieldName);
        Object lastElement = field instanceof Object[]
                ? ((Object[])field)[((Object[]) field).length - 1]
                : ((List)field).get(((List)field).size() - 1);
        assertTrue(checkValues(dataTable, lastElement));
    }

    @И("^У элемента \"([^\"]*)\" в поле \"([^\"]*)\" на (\\d+) месте элемент с полями:$")
    public void уЭлементаВПолеНаМестеЭлементСПолями(String key, String fieldName, int pos, DataTable dataTable) throws Throwable {
        Object obj = savedValues.get(key);
        Object field = ReflectionTestUtils.getField(obj, fieldName);
        Object nstElement = field instanceof Object[]
                ? ((Object[])field)[pos]
                : ((List)field).get(pos);
        assertTrue(checkValues(dataTable, nstElement));
    }
}
