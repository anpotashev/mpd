package ru.net.arh.mpd.model.events;

import ru.net.arh.mpd.model.sockjs.ResponseType;

public enum MpdEventType {
    CONNECTION_STATE_CHANGED("/topic/connection", ResponseType.CONNECTION_STATE)
    , GOT_IDLE_EVENT("/topic/idle", ResponseType.IDLE_EVENT)
    , PLAYER_CHANGED("/topic/player", ResponseType.PLAYER_CHANGED)
    , PLAYLIST_CHANGED("/topic/playlist", ResponseType.PLAYLIST_CHANGED)
    ;

    private String destination;

    private ResponseType responseType;

    MpdEventType(String destination, ResponseType responseType) {
        this.destination = destination;
        this.responseType = responseType;
    }

    public String getDestination() {
        return destination;
    }

    public ResponseType getResponseType() {
        return responseType;
    }
}
