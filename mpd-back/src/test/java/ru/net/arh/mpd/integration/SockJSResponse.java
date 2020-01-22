package ru.net.arh.mpd.integration;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeId;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;
import ru.net.arh.mpd.model.outputs.MpdOutput;
import ru.net.arh.mpd.model.playlist.Playlist;
import ru.net.arh.mpd.model.status.MpdShortStatus;
import ru.net.arh.mpd.model.status.MpdStatus;
import ru.net.arh.mpd.search.model.TreeItem;

@Getter
@Setter
public class SockJSResponse<T> {
    @JsonTypeId
    private String type;

    @JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = TreeItem.class, name = "FULL_TREE"),
            @JsonSubTypes.Type(value = TreeItem.class, name = "TREE"),
            @JsonSubTypes.Type(value = Boolean.class, name = "CONNECTION_STATE"),
            @JsonSubTypes.Type(value = Playlist.class, name = "PLAYLIST"),
            @JsonSubTypes.Type(value = Playlist[].class, name = "STORED_PLAYLISTS"),
            @JsonSubTypes.Type(value = Playlist.class, name = "STORED_PLAYLIST"),
            @JsonSubTypes.Type(value = MpdStatus.class, name = "STATUS"),
            @JsonSubTypes.Type(value = MpdShortStatus.class, name = "SONG_TIME"),
            @JsonSubTypes.Type(value = MpdOutput[].class, name = "OUTPUT")
    })
    private T payload;

}
