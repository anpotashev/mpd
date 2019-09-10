package ru.net.arh.mpd.model.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Событие, содержащее тип произошедшего изменения состоянии и обновленные данные.
 */
@Getter
@Setter
@AllArgsConstructor
public class MpdEvent<T> {

    /**
     * Тип события
     */
    MpdEventType type;
    /**
     * новые данные
     */
    T body;

}
