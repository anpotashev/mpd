package ru.net.arh.mpd.model.playlist;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.net.arh.mpd.model.MpdAnswer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@MpdAnswer(preffix = "playlist:")
public class Playlist {
    @Setter
    private List<PlaylistItem> playlistItems = new ArrayList<PlaylistItem>();
    @MpdAnswer(preffix = "playlist:")
    private String name;
    @MpdAnswer(preffix = "Last-Modified:")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime lastModified;

    public Playlist(List<PlaylistItem> playlistItems) {
        this.playlistItems = playlistItems;
    }
}
