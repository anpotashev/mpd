package ru.net.arh.mpd.model.commands;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Мультикоманда (группа однострончиков)
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class MpdMultiCommand extends MpdCommand {
    List<MpdCommand> commands;

    @Override
    public String toString() {
        return "command_list_begin\n" +
                commands.stream().map(cmd -> cmd.toString()).collect(Collectors.joining("\n"))
                + "\ncommand_list_end";
    }
}
