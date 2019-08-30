package ru.net.arh.mpd.model.status;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MpdShortStatus {

    private int songPos;
    private boolean playing;
    private MpdSongTime songTime;

}
