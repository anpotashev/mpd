package ru.net.arh.mpd.events;

/**
 * Сервис, отравляющий уведомления о событиях.
 */
public interface EventsService {

    /**
     * Событие, когда прошел успешный коннект
     */
    void onConnect();

    /**
     * Событие, когда прошел дисконнект
     */
    void onDisconnect();

    /**
     * Получено уведомление от mpd-сервера об изменениях в system.
     *
     * @param system
     */
    void onIdle(String system);
}
