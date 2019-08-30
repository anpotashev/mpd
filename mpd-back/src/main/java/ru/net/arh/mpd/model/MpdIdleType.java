package ru.net.arh.mpd.model;

public enum MpdIdleType {

    PLAYER("player")
    , PLAYLIST("playlist")
    ;

    private final String text;

    MpdIdleType(String text) {
        this.text = text;
    }

    public static MpdIdleType fromString(String text) {
        for (MpdIdleType b : MpdIdleType.values()) {
            if (b.text.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
