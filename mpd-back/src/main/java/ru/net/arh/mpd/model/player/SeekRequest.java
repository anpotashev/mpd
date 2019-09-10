package ru.net.arh.mpd.model.player;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SeekRequest {
    private int songPos;
    private int seekPos;
}
