package ru.net.arh.mpd.connection;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.net.arh.mpd.connection.rw.MpdReaderWriter;
import ru.net.arh.mpd.events.EventsService;
import ru.net.arh.mpd.model.BaseMpdCommand;
import ru.net.arh.mpd.model.MpdCommand;
import ru.net.arh.mpd.model.exception.MpdException;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
@Slf4j
@Primary
public class ConnectionServiceImplWithPool implements ConnectionService {

    private static final int MAX_COMMANDS_COUNT = 250;
    @Autowired
    private ConnectionSettings connectionSettings;
    @Autowired
    private EventsService eventsService;

    private static final int RW_COUNT = 3;

    private ExecutorService executor = Executors.newFixedThreadPool(10);
    private BlockingQueue<MpdReaderWriter> readerWriters = new LinkedBlockingQueue<>();
    private MpdReaderWriter idleReaderWriter;
    private List<MpdReaderWriter> allReaderWriters;

    @Getter
    private volatile boolean connected = false;

    @Override
    public void connect() {
        try {
            allReaderWriters = new ArrayList<>();
            for (int i = 0; i<RW_COUNT; i++) {
                Socket socket = new Socket(connectionSettings.getHost(), connectionSettings.getPort());
                MpdReaderWriter rw = new MpdReaderWriter(socket, connectionSettings.getPassword());
                allReaderWriters.add(rw);
                readerWriters.add(rw);
            }
            Socket socket = new Socket(connectionSettings.getHost(), connectionSettings.getPort());
            idleReaderWriter = new MpdReaderWriter(socket, connectionSettings.getPassword());
            allReaderWriters.add(idleReaderWriter);
            connected = true;
            executor = Executors.newFixedThreadPool(10);
            eventsService.onConnect();
        } catch (Exception e) {
            disconnect();
            throw new MpdException(e.getStackTrace().toString());
        }
    }

    @Override
    public void disconnect() {
        connected = false;
        executor.shutdown();
        readerWriters.clear();
        allReaderWriters.forEach(
                rw -> {try {rw.close();} catch (IOException e) {log.warn(e.getStackTrace().toString());}}
        );
        allReaderWriters.clear();
        idleReaderWriter = null;
        eventsService.onDisconnect();
    }

    @Override
    public List<String> sendCommand(BaseMpdCommand command) {
        if (!connected) {
            throw new MpdException("Not connected");
        }
        Callable<List<String>> task = new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                MpdReaderWriter rw = readerWriters.take();
                List<String> result;
                try {
                    result = rw.sendCommand(command);
                } catch (IOException e) {
                    disconnect();
                    throw new MpdException(e.getMessage());
                }
                readerWriters.put(rw);
                return result;
            }
        };
        Future<List<String>> feature = executor.submit(task);
        try {
            return feature.get();
        } catch (InterruptedException | ExecutionException e) {
            log.warn(e.getStackTrace().toString());
            throw new MpdException(e.getMessage());
        }
    }

    @Override
    public List<String> sendCommands(List<MpdCommand> commands) {
        //todo разбить при commands.size() > MAX_COMMANDS_COUNT
        //см БАГУЛИНА в ConnectionServiceImpl
        return sendCommand(MpdCommand.join(commands));
    }

    @Override
    public List<String> sendIdleCommand() {
        if (!connected) {
            throw new MpdException("Not connected");
        }
        try {
            return idleReaderWriter.sendCommand(MpdCommand.of(MpdCommand.Command.IDLE));
        } catch (IOException e) {
            disconnect();
            throw new MpdException(e.getMessage());
        }
    }
}
