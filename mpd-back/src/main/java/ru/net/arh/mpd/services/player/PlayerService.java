package ru.net.arh.mpd.services.player;

import ru.net.arh.mpd.model.player.PlayerCommand;

/**
 * Управление воспроизведением.
 */
public interface PlayerService {

    /**
     * Запустиь воспроизведение.
     */
    void play();

    /**
     * Поставить на паузу.
     */
    void pause();

    /**
     * Остановить воспроизведение.
     */
    void stop();

    /**
     * Перейти к предыдущему треку.
     */
    void prev();

    /**
     * Перейти к следующему треку.
     */
    void next();

    /**
     * Запустить воспроизведение трека по его позиции.
     *
     * @param songId
     */
    void playPos(int songId);

    /**
     * Запусить воспроизведение трека по его id.
     *
     * @param id
     */
    void playId(int id);

    /**
     * Запустить воспроизведение трека по его позиции со времени в секундах.
     *
     * @param seekPos - позиция трека в текущем плейлисте
     * @param songPos - время начала вопроизведения
     */
    void seek(int songPos, int seekPos);

    void doCommand(PlayerCommand playerCommand);
}