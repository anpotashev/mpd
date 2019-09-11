package ru.net.arh.mpd.model;

/**
 * Типы idle-событий, которые могут прийти от mpd-сервера. Перечисленны только обрабатываемые.
 */
public enum MpdIdleType {

    PLAYER("player"), PLAYLIST("playlist"), TREE("tree"), STATUS("status"), OUTPUT("output")
    , OPTIONS("options");

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
