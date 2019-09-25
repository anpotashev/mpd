package ru.net.arh.mpd.connection;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.context.annotation.Primary;
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
public abstract class ConnectionServiceImpl implements ConnectionService {

    private static final int MAX_COMMANDS_COUNT = 250;
    @Autowired
    private EventsService eventsService;

    @Getter
    private boolean connected = false;

    private MpdReaderWriter rw, idleRw;

    @Lookup
    abstract MpdReaderWriter getMpdReaderWriter();

    public void connect() {
        if (connected) {
            return;
        }
        try {
            rw = getMpdReaderWriter();
            idleRw = getMpdReaderWriter();
            connected = true;
            eventsService.onConnect();
        } catch (Exception e) {
            disconnect();
            throw new MpdException(e.getMessage());
        }
    }

    protected MpdReaderWriter createMpdReaderWriter(Socket socket, String password)
            throws IOException {
        return getMpdReaderWriter();
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
            List<String> result = commands.stream()
                    .collect(Collectors.groupingBy(i -> counter.getAndIncrement() / MAX_COMMANDS_COUNT))
                    .values()
                    .stream()
                    .map(cmd -> sendCommands(cmd))
                    .peek(list -> list.remove(list.get(list.size() - 1)))
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
            result.add("OK");
            return result;
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

    @Override
    public void ping() {
        sendCommand(MpdCommand.of(Command.PING));
    }
}