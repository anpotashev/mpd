package ru.net.arh.mpd.model.commands;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static ru.net.arh.mpd.model.commands.MpdCommandBuilder.*;

public class MpdCommandBuilderTest {

    @Test
    public void joinTest() {
        MpdCommand command1 = of(Command.PING);
        MpdCommand command2 = of(Command.ENABLE_OUTPUT).add(true);
        MpdCommand commman3 = of(Command.ADD).add(12, 13);
        MpdCommand actual = join(ImmutableList.of(command1, join(ImmutableList.of(command2, commman3))));
        String expected = new StringBuilder()
                .append("command_list_begin\n")
                .append("ping\n")
                .append("enableoutput \"1\"\n")
                .append("add \"12\" \"13\"\n")
                .append("command_list_end")
                .toString();
        assertEquals(actual.toString(), expected);
    }
}