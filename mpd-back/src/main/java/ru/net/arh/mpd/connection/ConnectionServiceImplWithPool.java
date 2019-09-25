package ru.net.arh.mpd.connection;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Service;
import ru.net.arh.mpd.connection.rw.MpdReaderWriter;
import ru.net.arh.mpd.events.EventsService;
import ru.net.arh.mpd.model.BaseMpdCommand;
import ru.net.arh.mpd.model.MpdCommand;
import ru.net.arh.mpd.model.exception.MpdException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
@Primary
@PropertySources({
        @PropertySource("classpath:/config/application.yaml")
        ,
        @PropertySource(value = "file:./custom/mpd.properties", ignoreResourceNotFound = true)
})
public abstract class ConnectionServiceImplWithPool implements ConnectionService {

    @Value("${mpdserver.maxCommandCount:${mpdserver.defaultMaxCommandCount}}")
    private final int MAX_COMMANDS_COUNT = 250;
    @Value("${mpdserver.maxConnectionCount:${mpdserver.defaultMaxConnectionCount}}")
    private final int RW_COUNT = 3;
    @Autowired
    private EventsService eventsService;

    private ExecutorService executor;
    private BlockingQueue<MpdReaderWriter> readerWriters = new LinkedBlockingQueue<>();
    private MpdReaderWriter idleReaderWriter;
    private List<MpdReaderWriter> allReaderWriters = new ArrayList<>();
    private ReentrantLock locker = new ReentrantLock();

    @Getter
    private volatile boolean connected = false;

    @Lookup
    abstract MpdReaderWriter getMpdReaderWriter();

    @Override
    public void connect() {
        if (connected) {
            return;
        }
        locker.lock();
        if (connected) {
            return;
        }
        try {
            allReaderWriters = new ArrayList<>();
            for (int i = 0; i<RW_COUNT; i++) {
                MpdReaderWriter rw = getMpdReaderWriter();
                allReaderWriters.add(rw);
                readerWriters.add(rw);
            }
            idleReaderWriter = getMpdReaderWriter();
            allReaderWriters.add(idleReaderWriter);
            executor = Executors.newFixedThreadPool(RW_COUNT);
            connected = true;
            eventsService.onConnect();
        } catch (Exception e) {
            disconnect();
            throw new MpdException(e.getMessage());
        } finally {
            locker.unlock();
        }
    }

    @Override
    public void disconnect() {
        connected = false;
        executor.shutdown();
        readerWriters.clear();
        allReaderWriters.forEach(
                rw -> {
                    try {
                        rw.close();
                    } catch (IOException e) {
                        log.warn("Exception on close: ", e);
                    }
                }
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
        Future<List<String>> feature = executor.submit(new SendCommandTask(command));
        try {
            return feature.get();
        } catch (InterruptedException | ExecutionException e) {
            log.warn(e.getMessage());
            throw new MpdException(e.getMessage());
        }
    }

    @Override
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
                    .peek(list -> list.remove(list.size() - 1))
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
            result.add("OK");
            return result;
        }
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

    @Override
    public void ping() {
        IntStream.range(0, RW_COUNT).forEach(value -> sendCommand(MpdCommand.of(MpdCommand.Command.PING)));
    }

    class SendCommandTask implements Callable<List<String>> {
        private final BaseMpdCommand command;

        SendCommandTask(BaseMpdCommand command) {
            this.command = command;
        }

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
    }
}
