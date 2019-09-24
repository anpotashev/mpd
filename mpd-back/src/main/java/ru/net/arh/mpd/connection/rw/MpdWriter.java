package ru.net.arh.mpd.connection.rw;

import ru.net.arh.mpd.model.MpdCommand;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

public class MpdWriter {
    private BufferedWriter writer;
    public MpdWriter(Writer writer) {
        this.writer = new BufferedWriter(writer);
    }

    public void writeCommand(MpdCommand command) throws IOException {
        writer.write(command.toString() + "\n");
        writer.flush();
    }
}
