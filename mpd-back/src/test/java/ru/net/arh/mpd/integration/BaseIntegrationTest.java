package ru.net.arh.mpd.integration;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import ru.net.arh.mpd.Main;
import ru.net.arh.mpd.connection.ConnectionService;
import ru.net.arh.mpd.connection.ConnectionSettings;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseIntegrationTest {

    @ClassRule
    public static GenericContainer genericContainer = new GenericContainer(
            new ImageFromDockerfile()
                    .withFileFromClasspath("mpd.conf", "docker/mpd/mpd.conf")
                    .withFileFromClasspath("Dockerfile", "docker/mpd/Dockerfile")
    )
            .withClasspathResourceMapping("docker/mpd/mpd", "/mpd", BindMode.READ_WRITE)
            .withClasspathResourceMapping("docker/mpd/music", "/music", BindMode.READ_WRITE)
            .withClasspathResourceMapping("docker/mpd/playlists", "/playlists", BindMode.READ_WRITE)
            .withExposedPorts(6600, 8000);

    @Autowired
    private ConnectionService connectionService;
    @MockBean
    ConnectionSettings connectionSettings;

    @BeforeClass
    public static void before() {
        genericContainer.start();
    }

    @Before
    public void beforeTest() {
        when(connectionSettings.getHost()).thenReturn("127.0.0.1");
        when(connectionSettings.getPassword()).thenReturn("12345678");
        when(connectionSettings.getPort()).thenReturn(genericContainer.getMappedPort(6600));
    }

    @Test
    public void  test() {

    }
}
