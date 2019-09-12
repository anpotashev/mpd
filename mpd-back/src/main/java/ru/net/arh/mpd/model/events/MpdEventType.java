package ru.net.arh.mpd.model.events;

import lombok.Getter;
import ru.net.arh.mpd.model.sockjs.ResponseType;

public enum MpdEventType {
    CONNECTION_STATE_CHANGED(ResponseType.CONNECTION_STATE, "/topic/connection")
    , PLAYLIST_CHANGED(ResponseType.PLAYLIST, "/topic/playlist")
    , STATUS_CHANGED(ResponseType.STATUS, "/topic/status")
    , FULL_TREE_CHANGED(ResponseType.FULL_TREE, "/topic/fullTree")
    , TREE_CHANGED(ResponseType.TREE, "/topic/tree")
    , OUTPUT(ResponseType.OUTPUT, "/topic/output")
    , SONG_TIME(ResponseType.SONG_TIME, "/topic/songTime")
    , STORED_PLAYLISTS(ResponseType.STORED_PLAYLISTS, "/topic/storedPlaylists");

    @Getter
    private final ResponseType responseType;
    @Getter
    private final String destionation;

    MpdEventType(ResponseType responseType, String destination) {
        this.responseType = responseType;
        this.destionation = destination;
    }
}
