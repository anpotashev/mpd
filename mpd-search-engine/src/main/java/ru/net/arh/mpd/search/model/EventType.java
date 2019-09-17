package ru.net.arh.mpd.search.model;

public enum EventType {
    SOCKS_CONNECTED,
    SOCKS_DISCONNECTED,
    FULL_TREE,
    CONNECTION_STATE;

    public <K> StompEvent<K> event(K body) {
        return new StompEvent<>(this, body);
    }
}