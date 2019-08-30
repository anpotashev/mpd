package ru.net.arh.mpd.model.playlist;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.net.arh.mpd.model.MpdAnswer;

@NoArgsConstructor
@Getter
@Setter
@MpdAnswer(preffix = "file:")
public class PlaylistItem {
    @MpdAnswer(preffix = "file:")
    private String file;
    @MpdAnswer(preffix = "Time:")
    private int time;
    @MpdAnswer(preffix = "Artist:")
    private String artist;
    @MpdAnswer(preffix = "Title:")
    private String title;
    @MpdAnswer(preffix = "Album:")
    private String album;
    @MpdAnswer(preffix = "Track:")
    private String track;
    @MpdAnswer(preffix = "Pos:")
    private int pos;
    @MpdAnswer(preffix = "Id:")
    private int id;
}
