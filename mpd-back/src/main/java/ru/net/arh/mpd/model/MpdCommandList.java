package ru.net.arh.mpd.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * модель для отправки группы команд как одной.
 * Лучше не использовать вне ConnectionServiceImpl: там при передаче слишком большого кол-ва команд,
 * они будут сгруппированы в несколько таких команд.
 * Для создания экземпляров используется метод MpdCommand.join(List<MpdCommand>)
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class MpdCommandList extends BaseMpdCommand {
    private List<MpdCommand> commands;
    @Override
    public String toString() {
        return "command_list_begin\n" +
                commands.stream().map(cmd -> cmd.toString()).collect(Collectors.joining("\n"))
                + "\ncommand_list_end";
    }
}
