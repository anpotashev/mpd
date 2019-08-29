package ru.net.arh.mpd.services.player;

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
     * Запусить воспроизведение трека по его id.
     * @param songId
     */
    void playPos(int songId);

    /**
     * Запустить воспроизведение трека по его позиции.
     * @param id
     */
    void playId(int id);

    /**
     * Запустить воспроизведение трека по его pos со времени seekPos.
     * @param seekPos
     * @param songPos
     */
    void seek(int seekPos, int songPos);
}