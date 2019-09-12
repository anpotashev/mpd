package ru.net.arh.mpd.model;

/**
 * Типы idle-событий, которые могут прийти от mpd-сервера. Перечисленны только обрабатываемые.
 */
public enum MpdIdleType {

    DATABASE("database"),
    STATUS("status"),
    UPDATE("update"),
    STORED_PLAYLIST("stored_playlist"),
    PLAYLIST("playlist"),
    PLAYER("player"),
    MIXER("mixer"),
    OUTPUT("output"),
    OPTIONS("options"),
    PARTITION("partition"),
    STICKER("sticker"),
    SUBSCRIPTION("subscription"),
    MESSAGE("message")
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
