package ru.net.arh.mpd.model;

import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Модель команды для отправки на mpd-сервер.
 */
@EqualsAndHashCode
public class MpdCommand {

    private Command command;

    private List<String> params = new ArrayList<>();

    public MpdCommand(Command command) {
        this.command = command;
    }

    public MpdCommand(Command command, String... params) {
        this(command);
        this.params = newArrayList(params);
    }

    public void addParam(String param) {
        params.add(param);
    }

    public void addParam(int param) {
        params.add(param + "");
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(command.str);
        params.stream()
                .map(s -> s.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\\"", "\\\\\\\""))
                .forEach(param -> builder.append(" \"" + param + "\""));
        return builder.toString();
    }

    public void addParam(boolean value) {
        addParam(value ? "1" : "0");
    }


    public enum Command {
        PLAY("play"),
        PAUSE("pause"),
        STOP("stop"),
        PREV("previous"),
        NEXT("next"),
        PLAYLIST_INFO("playlistinfo"),
        STATUS("status"),
        LSINFO("lsinfo"),
        IDLE("idle"),
        PING("ping"),
        ENABLE_OUTPUT("enableoutput"),
        DISABLE_OUTPUT("disableoutput"),
        OUTPUTS("outputs"),
        CLEAR("clear"),
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
        RENAME("rename")
        ;

        private String str;

        Command(String str) {
            this.str = str;
        }

        public MpdCommand build(String...params) {
            return new MpdCommand(this, params);
        }

    }
}