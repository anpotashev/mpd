package ru.net.arh.mpd.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
@AllArgsConstructor
public class MpdEvent<T> {

    MpdEventType type;
    T body;

}
