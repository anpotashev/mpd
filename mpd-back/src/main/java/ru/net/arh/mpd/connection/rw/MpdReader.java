package ru.net.arh.mpd.connection.rw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class MpdReader {
    private BufferedReader reader;

    public MpdReader(Reader reader) {
        this.reader = new BufferedReader(reader);
    }

    public List<String> readAnswer() throws IOException {
        List<String> result = new ArrayList<>();
        boolean done = false;
        while (!done) {
            String line = reader.readLine();
            done = line.startsWith("OK") || line.startsWith("ACK");
            result.add(line);
        }
        return result;
    }

}
