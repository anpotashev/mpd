package ru.net.arh.mpd.integration;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;
import ru.net.arh.mpd.Main;
import ru.net.arh.mpd.connection.ConnectionService;
import ru.net.arh.mpd.connection.ConnectionSettings;

import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@ContextConfiguration(classes = Main.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringCucumberIntegrationTest {

    @Value("${local.server.port}")
    int port;

    public static GenericContainer genericContainer = new GenericContainer("anpotashev/mpd4tests")
            .withExposedPorts(6600, 8000);

    static {
        genericContainer.start();
    }

    @Autowired
    ConnectionService connectionService;
    @Autowired
    @MockBean
    ConnectionSettings connectionSettings;

    @Before
    public void before() {
        initMocks(this);
        when(connectionSettings.getHost()).thenReturn("127.0.0.1");
        when(connectionSettings.getPassword()).thenReturn("12345678");
        when(connectionSettings.getPort()).thenReturn(genericContainer.getMappedPort(6600));

    }

    protected WsClient createClient() throws ExecutionException, InterruptedException {
        WsClient client = new WsClient("localhost", port);
        client.connect();
        return client;
    }
}
