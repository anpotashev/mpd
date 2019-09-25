package ru.net.arh.mpd.model.commands;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Билдер команд для отправки на mpd-сервер.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MpdCommandBuilder {

    private static Map<Command, MpdSingleCommand> cache = new HashMap<>();

    public static MpdMultiCommand join(List<MpdCommand> commands) {
        List<MpdCommand> simpleCommands = commands.stream()
                .flatMap(mpdCommand -> (Stream<MpdCommand>) (mpdCommand instanceof MpdSingleCommand
                        ? Stream.of(mpdCommand)
                        : ((MpdMultiCommand) mpdCommand).commands.stream()))
                .collect(Collectors.toList());
        return new MpdMultiCommand(simpleCommands);
    }

    public static MpdSingleCommand of(Command command) {
        if (command.acceptParam) {
            return new MpdSingleCommand(command);
        }
        if (!cache.containsKey(command)) {
            cache.putIfAbsent(command, new MpdSingleCommand(command));
        }
        return cache.get(command);
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
        PASSWORD("password");

        final String str;
        final boolean acceptParam; //флаг-маркер. false, если команда не принимает аргументов.
        // Используется для получаения инстанцов из пула, вместо создания новых.

        Command(String str) {
            this(str, true);
        }

        Command(String str, boolean acceptParam) {
            this.str = str;
            this.acceptParam = acceptParam;
        }

    }
}

