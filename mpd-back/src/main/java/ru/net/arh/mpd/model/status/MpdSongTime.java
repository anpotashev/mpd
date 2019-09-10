package ru.net.arh.mpd.model.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class MpdSongTime {
    private int current;
    private int full;
}
