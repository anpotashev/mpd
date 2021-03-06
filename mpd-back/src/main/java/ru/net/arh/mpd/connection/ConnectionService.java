package ru.net.arh.mpd.connection;

import ru.net.arh.mpd.model.commands.MpdCommand;

import java.util.List;

/**
 * Взаимодействие с mpd-сервером
 */
public interface ConnectionService {

    /**
     * Проверка, установлено-ли соединение
     *
     * @return
     */
    boolean isConnected();

    /**
     * try connect
     */
    void connect();

    /**
     * disconnect
     */
    void disconnect();

    /**
     * Отправить комнаду
     *
     * @param command
     * @return массив строк, который вернул сервер
     */
    List<String> sendCommand(MpdCommand command);

    List<String> sendCommands(List<MpdCommand> commands);

    /**
     * Отправить команду idle
     *
     * @return массив строк, который вернул сервер
     */
    List<String> sendIdleCommand();

    void ping();
}