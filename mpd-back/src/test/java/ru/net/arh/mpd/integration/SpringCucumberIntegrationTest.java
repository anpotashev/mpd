package ru.net.arh.mpd.integration;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
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

//    @ClassRule
    public static GenericContainer genericContainer = new GenericContainer(
            new ImageFromDockerfile()
                    .withFileFromClasspath("mpd.conf", "docker/mpd/mpd.conf")
                    .withFileFromClasspath("Dockerfile", "docker/mpd/Dockerfile")
    )
            .withClasspathResourceMapping("docker/mpd/mpd", "/mpd", BindMode.READ_WRITE)
            .withClasspathResourceMapping("docker/mpd/music", "/music", BindMode.READ_WRITE)
            .withClasspathResourceMapping("docker/mpd/playlists", "/playlists", BindMode.READ_WRITE)
            .withExposedPorts(6600, 8000);

    static {
        genericContainer.start();
    }

    @Autowired
    ConnectionService connectionService;
    @Autowired
    @MockBean
    ConnectionSettings connectionSettings;

//    WsClient client;

    @Before
    public void before() {
        initMocks(this);
        when(connectionSettings.getHost()).thenReturn("127.0.0.1");
        when(connectionSettings.getPassword()).thenReturn("12345678");
        when(connectionSettings.getPort()).thenReturn(genericContainer.getMappedPort(6600));

    }

//    public void wsConnect() throws ExecutionException, InterruptedException {
//        client.connect();
//    }
//
//    public void sendCommand(String command) {
//        client.sendCommand(command);
//    }
//
//    public void subscribe(String s, StompFrameHandler handler) {
//        client.subscribe(s, handler);
//    }

    protected WsClient createClient() throws ExecutionException, InterruptedException {
        WsClient client = new WsClient("localhost", port);
        client.connect();
        return client;
    }
}
