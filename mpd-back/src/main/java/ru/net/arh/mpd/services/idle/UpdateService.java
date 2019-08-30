package ru.net.arh.mpd.services.idle;

import ru.net.arh.mpd.model.events.MpdEvent;

/**
 * Синтетический сервис, со множеством имплементаций.
 * Вызов метода происходит при получении события idle от mpd-сервера.
 * Какие имплементации будут при этом заторнуты, зависит от типа события
 */
public interface UpdateService {
    /**
     * Инициирует запрос информации к mpd серверу. Тип запроса зависит от имплементации.
     * @return событие, которое передаст клиентам новое состояние.
     */
    MpdEvent createUpdateEvent();
}
