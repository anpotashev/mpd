package ru.net.arh.mpd.connection.rw;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import ru.net.arh.mpd.model.BaseMpdCommand;
import ru.net.arh.mpd.model.MpdCommand;
import ru.net.arh.mpd.model.exception.MpdException;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.net.arh.mpd.model.MpdCommand.Command.PASSWORD;

@Slf4j
public class MpdReaderWriter implements Closeable {

    private MpdReader reader;
    private MpdWriter writer;
    private Socket socket;
    private static final Pattern VERSION_PATTERN = Pattern.compile("OK MPD (.*)");
    @Getter
    private String version;

    public MpdReaderWriter(Socket socket, String password) throws IOException {
        this.socket = socket;
        log.debug("creating MpdReaderWriter object");
        reader = new MpdReader(new InputStreamReader(this.socket.getInputStream(), Charset.forName("UTF-8")));
        writer = new MpdWriter(new OutputStreamWriter(this.socket.getOutputStream(), Charset.forName("UTF-8")));
        version = readMpdVersion();
        if (StringUtils.isNotEmpty(password)) {
            sendCommand(MpdCommand.of(PASSWORD).add(password));
        }
    }

    private String readMpdVersion() throws IOException {
        List<String> strings = reader.readAnswer();
        if (strings.size() != 1) {
            log.error("On connect got answer: ", strings);
            throw new MpdException("Got impossible answer");
        }
        Matcher matcher = VERSION_PATTERN.matcher(strings.get(0));
        if (matcher.find()) {
            return matcher.group(0);
        }
        log.error("got unknown answer on connect: {}", strings.get(0));
        throw new MpdException("got unknown answer on connect: {}", strings.get(0));
    }

    public List<String> sendCommand(BaseMpdCommand command) throws IOException {
        writer.writeCommand(command);
        List<String> strings = reader.readAnswer();
        String last = strings.get(strings.size() - 1);
        if (strings.get(strings.size()-1).startsWith("OK")) {
            return strings;
        }
        log.info("On sending command: '{}' got error '{}'", command, last.substring(3).trim());
        throw new MpdException("On sending command: '{}' got error '{}'", command, last.substring(3).trim());
    }

    public void disconnect() throws IOException {
        log.debug("closing connection");
        socket.close();
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }
}