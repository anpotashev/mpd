package ru.net.arh.mpd.search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StompEvent<T> {
    EventType type;
    T body;
}
