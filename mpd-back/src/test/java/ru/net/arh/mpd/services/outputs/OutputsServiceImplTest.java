package ru.net.arh.mpd.services.outputs;

import com.google.common.collect.ImmutableList;
import org.assertj.core.api.Assertions;
import org.checkerframework.common.value.qual.IntRange;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import ru.net.arh.mpd.connection.ConnectionService;
import ru.net.arh.mpd.model.commands.MpdCommand;
import ru.net.arh.mpd.model.commands.MpdCommandBuilder;
import ru.net.arh.mpd.model.commands.MpdSingleCommand;
import ru.net.arh.mpd.model.outputs.MpdOutput;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class OutputsServiceImplTest {

    private static final List<String> OUTPUTS = ImmutableList.of(
            "outputid: 0"
            , "outputname: My HTTP Stream"
            , "outputenabled: 1"
            , "outputid: 1"
            , "outputname: Second HTTP Stream"
            , "outputenabled: 0"
            , "OK"
    );
    @Mock
    private ConnectionService connectionService;

    @InjectMocks
    private OutputsServiceImpl outputsService;

    @Before
    public void before() {
        initMocks(this);
    }

    @Captor
    ArgumentCaptor<MpdCommand> captor;

    @Test
    public void outputs() {
        when(connectionService.sendCommand(any())).thenReturn(OUTPUTS);
        List<MpdOutput> outputs = outputsService.outputs();
        List<MpdOutput> expected = ImmutableList.of(newMpdOutput(0, "My HTTP Stream", true),
                newMpdOutput(1,  "Second HTTP Stream", false));
        assertEquals(outputs.size(), expected.size());
        IntStream.range(0, outputs.size()).forEach(i -> Assertions.assertThat(outputs.get(i)).isEqualToComparingFieldByField(expected.get(i)));
    }

    private MpdOutput newMpdOutput(int id, String name, boolean enabled) {
        MpdOutput result = new MpdOutput();
        result.setId(id);
        result.setName(name);
        result.setEnabled(enabled);
        return result;
    }

    @Test
    public void save() {
        MpdOutput output = newMpdOutput(0, "My HTTP Stream", true);
        outputsService.save(output);
        verify(connectionService, times(1)).sendCommand(captor.capture());
        verifyNoMoreInteractions(connectionService);
        reset(connectionService);
        assertTrue(captor.getValue() instanceof MpdSingleCommand);
        MpdSingleCommand mpdCommand = (MpdSingleCommand) captor.getValue();
        MpdSingleCommand expectedCommand = MpdCommandBuilder.of(MpdCommandBuilder.Command.ENABLE_OUTPUT).add(0);
        Assertions.assertThat(mpdCommand).isEqualToComparingFieldByField(expectedCommand);

        output = newMpdOutput(0, "My HTTP Stream", false);
        outputsService.save(output);
        verify(connectionService, times(1)).sendCommand(captor.capture());
        assertTrue(captor.getValue() instanceof MpdSingleCommand);
        mpdCommand = (MpdSingleCommand) captor.getValue();
        expectedCommand = MpdCommandBuilder.of(MpdCommandBuilder.Command.DISABLE_OUTPUT).add(0);
        Assertions.assertThat(mpdCommand).isEqualToComparingFieldByField(expectedCommand);
    }
}