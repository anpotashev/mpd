package ru.net.arh.mpd.model.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MpdEvent<T> {

    MpdEventType type;
    T body;

}
