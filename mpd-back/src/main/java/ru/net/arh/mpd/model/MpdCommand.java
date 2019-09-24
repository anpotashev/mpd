package ru.net.arh.mpd.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Модель команды для отправки на mpd-сервер.
 */
@EqualsAndHashCode
public class MpdCommand {

    private static Map<Command, MpdCommand> cache = new HashMap<>();

    private Command command;

    private List<String> params = new ArrayList<>();

    public MpdCommand() {
    }

    private MpdCommand(Command command) {
        this.command = command;
    }

    public static MpdCommand of(List<MpdCommand> commands) {
        return new MpdListCommand(commands);
    }

    public static MpdCommand of(Command command) {
        if (command.acceptParam) {
            return new MpdCommand(command);
        }
        if (!cache.containsKey(command)) {
            cache.putIfAbsent(command, new MpdCommand(command));
        }
        return cache.get(command);
    }

    public MpdCommand add(String param) {
        this.addParam(param);
        return this;
    }
    public MpdCommand add(String...params) {
        Arrays.stream(params).forEach(this::add);
        return this;
    }

    public MpdCommand add(boolean param) {
        return this.add(param ? "1" : "0");
    }

    public MpdCommand add(int param) {
        return this.add(param + "");
    }

    public MpdCommand add(int...params) {
        Arrays.stream(params).forEach(this::add);
        return this;
    }

    private void addParam(String param) {
        if (this.command.acceptParam) {
            params.add(param);
            return;
        }
        //Попали сюда,значит косяк в сырцах
        throw new Error("Command " + command + " doesn't accept parameters");
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(command.str);
        params.stream()
                .map(s -> s.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\\"", "\\\\\\\""))
                .forEach(param -> builder.append(" \"" + param + "\""));
        return builder.toString();
    }

    public enum Command {
        PLAY("play", false),
        PAUSE("pause", false),
        STOP("stop", false),
        PREV("previous", false),
        NEXT("next", false),
        PLAYLIST_INFO("playlistinfo"),
        STATUS("status", false),
        LSINFO("lsinfo", false),
        IDLE("idle", false),
        PING("ping", false),
        ENABLE_OUTPUT("enableoutput"),
        DISABLE_OUTPUT("disableoutput"),
        OUTPUTS("outputs"),
        CLEAR("clear", false),
        DELETE("delete"),
        MOVE("move"),
        SHUFFLE("shuffle"),
        ADD("add"),
        ADD_ID("addid"),
        PLAY_ID("playid"),
        SEEK("seek"),
        LISTALL("listall"),
        LISTALLINFO("listallinfo"),
        UPDATE("update"),
        LISTPLAYLISTS("listplaylists"),
        LISTPLAYLIST_INFO("listplaylistinfo"),
        RANDOM("random"),
        REPEAT("repeat"),
        SINGLE("single"),
        CONSUME("consume"),
        LOAD("load"),
        RM("rm"),
        SAVE("save"),
        RENAME("rename"),
        PASSWORD("password")
        ;

        private String str;
        private boolean acceptParam;

        Command(String str) {
            this(str, true);
        }
        Command(String str, boolean acceptParam) {
            this.str = str;
            this.acceptParam = acceptParam;
        }

    }

    private static class MpdListCommand extends MpdCommand {
        private List<MpdCommand> commands;

        public MpdListCommand(List<MpdCommand> commands) {
            this.commands = commands;
        }

        @Override
        public String toString() {
            return "command_list_begin\n" +
                commands.stream().map(cmd -> cmd.toString()).collect(Collectors.joining("\n"))
                + "\ncommand_list_end";
        }
    }
}