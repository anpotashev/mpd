package ru.net.arh.mpd.model.status;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.net.arh.mpd.model.MpdAnswer;

@Slf4j
@Getter
@Setter
public class MpdStatus {
    @MpdAnswer(preffix = "volume:")
    private int volume;
    @MpdAnswer(preffix = "repeat:")
    private boolean repeat;
    @MpdAnswer(preffix = "random:")
    private boolean random;
    @MpdAnswer(preffix = "single:")
    private boolean single;
    @MpdAnswer(preffix = "consume:")
    private boolean consume;
    @MpdAnswer(preffix = "playlist:")
    private String playlist;
    @MpdAnswer(preffix = "playlistlength:")
    private int playlistlength;
    @MpdAnswer(preffix = "xfade:")
    private int xfade;
    @MpdAnswer(preffix = "state:")
    private String state;
    @MpdAnswer(preffix = "song:")
    private int song;
    @MpdAnswer(preffix = "songid:")
    private int songid;
    @JsonIgnore
    @MpdAnswer(preffix = "time:")
    private MpdSongTime time;
    @MpdAnswer(preffix = "bitrate:")
    private int bitrate;
    @MpdAnswer(preffix = "audio:")
    private String audio;
    @MpdAnswer(preffix = "nextsong:")
    private int nextsong;
    @MpdAnswer(preffix = "nextsongid:")
    private int nextsongid;
}
