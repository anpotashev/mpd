package ru.net.arh.mpd.stomp;

/**
 * Сервис работающий с "подписчиками websocket-соединения
 */
public interface WsSubscribersService {

    /**
     * Проверяет есть-ли подписчики
     *
     * @param destination
     * @return
     */
    boolean isSubscribersFound(String destination);
}
