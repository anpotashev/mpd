package ru.net.arh.mpd.connection;

import com.google.common.collect.ImmutableList;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.springframework.util.ReflectionUtils;
import ru.net.arh.mpd.connection.rw.MpdReaderWriter;
import ru.net.arh.mpd.events.EventsService;
import ru.net.arh.mpd.model.commands.MpdCommand;
import ru.net.arh.mpd.model.commands.MpdCommandBuilder;
import ru.net.arh.mpd.model.commands.MpdMultiCommand;
import ru.net.arh.mpd.model.commands.MpdSingleCommand;
import ru.net.arh.mpd.model.exception.MpdException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static ru.net.arh.mpd.model.commands.MpdCommandBuilder.Command.IDLE;
import static ru.net.arh.mpd.model.commands.MpdCommandBuilder.Command.PING;

public class ConnectionServiceImplTest {

    @Mock
    private MpdReaderWriter mpdReaderWriter;
    @Mock
    private EventsService eventsService;
    @InjectMocks
    private ConnectionService connectionService = new ConnectionServiceImpl() {
        @Override
        MpdReaderWriter getMpdReaderWriter() {
            return mpdReaderWriter;
        }
    };

    @InjectMocks

    private ConnectionService connectionService2 = new ConnectionServiceImpl() {
        @Override
        public MpdReaderWriter getMpdReaderWriter() throws IOException {
            throw new IOException();
        }
    };
    private final int commandCount = 10;
    private final int connectionCount = 3;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        initMocks(this);
        setVariablesInConnectionService((ConnectionServiceImpl) connectionService);
        setVariablesInConnectionService((ConnectionServiceImpl) connectionService2);
    }

    private void setVariablesInConnectionService(ConnectionServiceImpl connectionService) {
        Field maxCommandsCount = ReflectionUtils.findField(ConnectionServiceImpl.class, "MAX_COMMANDS_COUNT", int.class);
        maxCommandsCount.setAccessible(true);
        ReflectionUtils.setField(maxCommandsCount, connectionService, commandCount);
        maxCommandsCount.setAccessible(false);
        Field rwCount = ReflectionUtils.findField(ConnectionServiceImpl.class, "RW_COUNT", int.class);
        rwCount.setAccessible(true);
        ReflectionUtils.setField(rwCount, connectionService, connectionCount);
        rwCount.setAccessible(false);
    }

    /**
     * Коннект без ошибок
     */
    @Test
    public void connect() {
        assertFalse(connectionService.isConnected());
        connectionService.connect();
        verify(eventsService, times(1)).onConnect();
        assertTrue(connectionService.isConnected());
        connectionService.disconnect();
        verify(eventsService, times(1)).onDisconnect();
        assertFalse(connectionService.isConnected());
        verifyNoMoreInteractions(eventsService);
    }

    /**
     * Коннект с ошибкой
     */
    @Test
    public void testConnectWithException() {
        try {
            connectionService2.connect();
        } catch (MpdException e) {
            assertFalse(connectionService2.isConnected());
            verify(eventsService, times(1)).onDisconnect();
            verifyNoMoreInteractions(eventsService);
        }
    }

    /**
     * Отправка простой команды
     */
    @Test
    public void sendCommand() throws IOException {
        when(mpdReaderWriter.sendCommand(any(MpdSingleCommand.class))).thenReturn(ImmutableList.of("OK"));
        connectionService.connect();
        List<String> strings = connectionService.sendCommand(MpdCommandBuilder.of(PING));
        assertEquals(strings.size(), 1);
        assertEquals(strings.get(0), "OK");
    }

    /**
     * Отправка команды закончилась IOException
     * Должен пройти дисконнект, а ошибку обернуть в MpdException
     */
    @Test
    public void sendCommandWithIOException() throws IOException {
        when(mpdReaderWriter.sendCommand(any(MpdSingleCommand.class))).thenThrow(new IOException());
        connectionService.connect();
        verify(eventsService, times(1)).onConnect();
        try {
            connectionService.sendCommand(MpdCommandBuilder.of(PING));
        } catch (MpdException e) {
            assertFalse(connectionService.isConnected());
            verify(eventsService, times(1)).onDisconnect();
            verifyNoMoreInteractions(eventsService);
        }
    }

    /**
     * Отправка команды закончилась MpdException
     * Ошибку должено пробросить дальше, дисконнекта быть не должно.
     */
    @Test
    public void sendCommandWithMpdException() throws IOException {
        connectionService.connect();
        when(mpdReaderWriter.sendCommand(any(MpdSingleCommand.class))).thenThrow(new MpdException("Some error text"));
        try {
            connectionService.sendCommand(MpdCommandBuilder.of(PING));
        } catch (MpdException e) {
            assertEquals( "Some error text", e.getMessage());
            assertTrue(connectionService.isConnected());
        }
    }

    /**
     * Отправка команды, когда нет соединения
     */
    @Test
    public void sendCommandOnNotConnected() throws IOException {
        thrown.expect(MpdException.class);
        thrown.expectMessage("Not connected");
        connectionService.sendCommand(MpdCommandBuilder.of(PING));
    }

    /**
     * Отправка списка команд
     */
    @Test
    public void sendCommandsTest() throws IOException {
        List<String> singleAnswer = ImmutableList.of("ASDF", "OK");
        when(mpdReaderWriter.sendCommand(any(MpdCommand.class))).thenReturn(singleAnswer);
        connectionService.connect();
        List<String> strings = connectionService.sendCommand(MpdCommandBuilder.join(Collections.nCopies(3, MpdCommandBuilder.of(PING))));
        assertEquals(strings.size(), singleAnswer.size());
        Assertions.assertThat(strings).isEqualTo(singleAnswer);
    }

    /**
     * Отправка списка команд
     * Отправка команды закончилась IOException
     * Должен пройти дисконнект, а ошибку обернуть в MpdException
     */
    @Test
    public void sendCommandsWithIoExceptionTest() throws IOException {
        when(mpdReaderWriter.sendCommand(any(MpdCommand.class))).thenThrow(new IOException());
        connectionService.connect();
        verify(eventsService, times(1)).onConnect();
        try {
            connectionService.sendCommand(MpdCommandBuilder.join(Collections.nCopies(3, MpdCommandBuilder.of(PING))));
        } catch (MpdException e) {
            verify(eventsService, times(1)).onDisconnect();
            verifyNoMoreInteractions(eventsService);
            assertFalse(connectionService.isConnected());
        }
    }

    /**
     * Отправка списка команд
     * Отправка команды закончилась MpdException
     * Должен ошибку пробросить дальше. Дисконнекта быть не должно
     */
    @Test
    public void sendCommandsWithMpdExceptionTest() throws IOException {
        when(mpdReaderWriter.sendCommand(any(MpdCommand.class))).thenThrow(new MpdException("Some text"));
        connectionService.connect();
        try {
            connectionService.sendCommand(MpdCommandBuilder.join(Collections.nCopies(3, MpdCommandBuilder.of(PING))));
        } catch (MpdException e) {
            assertEquals(e.getMessage(), "Some text");
            assertTrue(connectionService.isConnected());
        }
    }

    /**
     * Отправка списка команд, по размеру превышающий максимальное кол-во команд
     *
     * @throws IOException
     */
    @Test
    public void sendCommandsLongTest() throws IOException {
        List<String> singleAnswer = new ArrayList<>(ImmutableList.of("ASDF"));
        when(mpdReaderWriter.sendCommand(any(MpdMultiCommand.class))).thenAnswer((Answer<List<String>>) invocation -> {
            MpdMultiCommand mpdMultiCommand = invocation.getArgument(0);
            Field field = ReflectionUtils.findField(MpdMultiCommand.class, "commands", List.class);
            field.setAccessible(true);
            List<MpdSingleCommand> mpdSingleCommands = (List<MpdSingleCommand>) ReflectionUtils.getField(field, mpdMultiCommand);
            field.setAccessible(false);
            List<String> result = new ArrayList<>(Collections.nCopies(mpdSingleCommands.size(), "ASDF"));
            return result;
        });
        connectionService.connect();
        List<String> actual = connectionService.sendCommands(Collections.nCopies(commandCount + 1, MpdCommandBuilder.of(PING)));
        verify(mpdReaderWriter, times(2)).sendCommand(any(MpdCommand.class));
        verifyNoMoreInteractions(mpdReaderWriter);
        assertEquals(actual.size(), singleAnswer.size() * (commandCount + 1));
        List<String> expected = new ArrayList<>(Collections.nCopies(commandCount + 1, "ASDF"));
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    /**
     * Отправка idle команды.
     */
    @Test
    public void sendIdleCommand() throws IOException {
        connectionService.connect();
        ImmutableList<String> answer = ImmutableList.of("changed: player", "OK");
        when(mpdReaderWriter.sendCommand(MpdCommandBuilder.of(IDLE))).thenReturn(answer);
        List<String> actual = connectionService.sendIdleCommand();
        Assertions.assertThat(actual).isEqualTo(answer);
        verify(mpdReaderWriter, times(1)).sendCommand(MpdCommandBuilder.of(IDLE));
        verifyNoMoreInteractions(mpdReaderWriter);
    }

    /**
     * Отправка idle команды. Заканчивается IOException на mpdReaderWriter.
     * Должен проийти дисконнект. В ответ на idle-каманду должен вернуться пустой лист
     */
    @Test
    public void sendIdleCommandWithIoExecption() throws IOException {
        connectionService.connect();
        verify(eventsService, times(1)).onConnect();
        when(mpdReaderWriter.sendCommand(MpdCommandBuilder.of(IDLE))).thenThrow(new IOException());
        List<String> list = connectionService.sendIdleCommand();
        assertTrue(list.isEmpty());
        assertFalse(connectionService.isConnected());
        verify(eventsService, times(1)).onDisconnect();
        verifyNoMoreInteractions(eventsService);
    }
    /**
     * Отправка idle команды. Заканчивается MpdException на mpdReaderWriter.
     * MpdExeception должен быть проброшен. Дисконнекта быть не должно.
     *
     * В "природе" такой кейс возможен, только, если с указанными параметрами соединения нет доступа к команде idle.
     */
    @Test
    public void sendIdleCommandWithMpdExecption() throws IOException {
        connectionService.connect();
        verify(eventsService, times(1)).onConnect();
        when(mpdReaderWriter.sendCommand(MpdCommandBuilder.of(IDLE))).thenThrow(new MpdException("some text"));
        try {
            connectionService.sendIdleCommand();
        } catch (MpdException e) {
            assertEquals(e.getMessage(), "some text");
            assertTrue(connectionService.isConnected());
            verifyNoMoreInteractions(eventsService);
        }
    }

    /**
     * Проверка команды пинг. Должна "уйти" столько раз, сколько указано в параметре "кол-во соединений"
     * (mpdserver.maxConnectionCount)
     */
    @Test
    public void ping() throws IOException {
        connectionService.connect();
        connectionService.ping();
        verify(mpdReaderWriter, times(connectionCount)).sendCommand(MpdCommandBuilder.of(PING));
        verifyNoMoreInteractions(mpdReaderWriter);
    }
}