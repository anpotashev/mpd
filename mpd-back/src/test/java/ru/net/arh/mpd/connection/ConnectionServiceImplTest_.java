package ru.net.arh.mpd.connection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.net.arh.mpd.connection.rw.MpdReaderWriter;
import ru.net.arh.mpd.connection.rw.MpdReaderWriterMockImpl;
import ru.net.arh.mpd.model.commands.MpdCommandBuilder;

import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ConnectionServiceImplTest_ {

    @Autowired
    private MpdReaderWriter mpdReaderWriter;
    @Autowired
    private ConnectionService connectionService;

    @Before
    public void setUp() throws Exception {
        ((MpdReaderWriterMockImpl)mpdReaderWriter).loadYaml("mpd.yaml");
    }

    @Test
    public void connect() {
        connectionService.connect();
        List<String> strings = connectionService.sendCommand(MpdCommandBuilder.of(MpdCommandBuilder.Command.PASSWORD).add("12345678"));
        System.out.println(strings);
    }

    @Test
    public void disconnect() {
    }

    @Test
    public void sendCommand() {
    }

    @Test
    public void sendCommands() {
    }

    @Test
    public void sendIdleCommand() {
    }

    @Test
    public void ping() {
    }

    @Test
    public void isConnected() {
    }
}