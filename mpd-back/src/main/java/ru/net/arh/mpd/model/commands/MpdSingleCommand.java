package ru.net.arh.mpd.model.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Однострочная команда
 */
public class MpdSingleCommand extends MpdCommand {
    private MpdCommandBuilder.Command command;

    private List<String> params = new ArrayList<>();

    MpdSingleCommand(MpdCommandBuilder.Command command) {
        this.command = command;
    }

    public MpdSingleCommand add(String param) {
        params.add(param);
        return this;
    }

    public MpdSingleCommand add(boolean param) {
        return this.add(param ? "1" : "0");
    }

    public MpdSingleCommand add(int param) {
        return this.add(param + "");
    }

    public MpdSingleCommand add(int... params) {
        Arrays.stream(params).forEach(this::add);
        return this;
    }

    public MpdSingleCommand add(String... params) {
        Arrays.stream(params).forEach(this::add);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(command.str);
        params.stream()
                .map(s -> s.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\\"", "\\\\\\\""))
                .forEach(param -> builder.append(" \"" + param + "\""));
        return builder.toString();
    }
}
