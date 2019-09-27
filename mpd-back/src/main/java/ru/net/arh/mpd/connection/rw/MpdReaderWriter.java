package ru.net.arh.mpd.connection.rw;

import ru.net.arh.mpd.model.commands.MpdCommand;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public interface MpdReaderWriter extends Closeable {
    List<String> sendCommand(MpdCommand command) throws IOException;
}
