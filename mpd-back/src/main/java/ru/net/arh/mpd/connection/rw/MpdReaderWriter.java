package ru.net.arh.mpd.connection.rw;

import lombok.extern.slf4j.Slf4j;
import ru.net.arh.mpd.model.exception.MpdException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MpdReaderWriter {

    private BufferedReader reader;
    private BufferedWriter writer;
    private Socket socket;

    public MpdReaderWriter(Socket socket, String password) throws IOException {
        this.socket = socket;
        log.debug("creating MpdReaderWriter object");
        reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), Charset.forName("UTF-8")));
        writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream(), Charset.forName("UTF-8")));
        readMpdVersion();
        if (password != null && !password.isEmpty()) {
            enterPassword(password);
        }
    }

    private void readMpdVersion() throws IOException {
        String s = reader.readLine();
        log.debug("got '{}' on connect", s);
        if (!s.startsWith("OK")) {
            throw new MpdException("don't get OK answer on connect");
        }
    }

    private void enterPassword(String password) throws IOException {
        List<String> list = sendCommand("password " + password);
        if (!list.get(list.size() - 1).startsWith("OK")) {
            throw new MpdException("don't get OK answer on password");
        }
    }

    public List<String> sendCommand(String command) throws IOException {
        writer.write(command + "\n");
        writer.flush();
        List<String> result = new ArrayList<String>();
        while (true) {
            String s = reader.readLine();
            result.add(s);
            if (s.startsWith("OK")) {
                log.debug(
                        "got result for '{}' command. Result: {}'",
                        command,
                        result.subList(Math.max(result.size() - 10, 0), result.size())
                );
                return result;
            }
            if (s.startsWith("ACK")) {
                log.info("On sending command: '{}' got error '{}'", command, s.substring(3).trim());
                throw new MpdException("On sending command: '{}' got error '{}'", command, s.substring(3).trim());
            }
        }
    }

    public void disconnect() throws IOException {
        log.debug("closing connection");
        socket.close();
    }
}