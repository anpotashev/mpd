package ru.net.arh.mpd.connection;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.net.arh.mpd.connection.rw.MpdReaderWriter;
import ru.net.arh.mpd.events.EventsService;
import ru.net.arh.mpd.model.BaseMpdCommand;
import ru.net.arh.mpd.model.MpdCommand;
import ru.net.arh.mpd.model.MpdCommand.Command;
import ru.net.arh.mpd.model.exception.MpdException;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ConnectionServiceImpl implements ConnectionService {

    private static final int MAX_COMMANDS_COUNT = 250;
    @Autowired
    private ConnectionSettings connectionSettings;
    @Autowired
    private EventsService eventsService;

    @Getter
    private boolean connected = false;

    private MpdReaderWriter rw, idleRw;

    public void connect() {
        if (connected) {
            return;
        }
        try {
            Socket socket = createSocket(connectionSettings.getHost(), connectionSettings.getPort());
            rw = createMpdReaderWriter(socket, connectionSettings.getPassword());
            Socket socket2 = createSocket(connectionSettings.getHost(), connectionSettings.getPort());
            idleRw = createMpdReaderWriter(socket2, connectionSettings.getPassword());
            connected = true;
            eventsService.onConnect();
        } catch (IOException e) {
            disconnect();
            throw new MpdException(e.getMessage());
        }
    }

    protected MpdReaderWriter createMpdReaderWriter(Socket socket, String password)
            throws IOException {
        return new MpdReaderWriter(socket, password);
    }

    protected Socket createSocket(String host, Integer port) throws IOException {
        return new Socket(host, port);
    }

    public void disconnect() {
        if (!connected) {
            return;
        }
        try {
            rw.disconnect();
        } catch (IOException e) {
            log.warn("IO exception during disconnect");
        } finally {
            rw = null;
        }
        try {
            idleRw.disconnect();
        } catch (IOException e) {
            log.warn("IO exception during disconnect");
        } finally {

            idleRw = null;
            connected = false;
            eventsService.onDisconnect();
        }
    }

    public List<String> sendCommand(BaseMpdCommand command) {
        if (!connected) {
            throw new MpdException("Not connected");
        }
        try {
            return send(command);
        } catch (IOException e) {
            disconnect();
            throw new MpdException("IOException on sending command '" + command.toString() + "'. Disconnected.");
        }
    }

    public List<String> sendCommands(List<MpdCommand> commands) {
        if (!connected) {
            throw new MpdException("Not connected");
        }
        if (commands.size()>MAX_COMMANDS_COUNT) {
            AtomicInteger counter = new AtomicInteger(0);
            commands.stream()
                    .collect(Collectors.groupingBy(i -> counter.getAndIncrement() / MAX_COMMANDS_COUNT))
                    .values().stream().forEach(cmd -> sendCommands(cmd));
        }
        try {
            return send(MpdCommand.join(commands));
        } catch (IOException e) {
            disconnect();
            throw new MpdException("IOException on sending command '" + commands.toString() + "'. Disconnected.");
        }
    }


    private synchronized List<String> send(BaseMpdCommand cmd) throws IOException {
        return rw.sendCommand(cmd);
    }

    @Override
    public List<String> sendIdleCommand() {
        if (!connected) {
            throw new MpdException("Not connected");
        }
        try {
            return idleRw.sendCommand(MpdCommand.of(Command.IDLE));
        } catch (IOException e) {
            disconnect();
            throw new MpdException("IOException on sending command 'idle'. Disconnected.");
        }

    }
}