package ru.net.arh.mpd.search.model;

public enum Id3Tag {
    ARTIST,
    ALBUM
    ;

    public Condition contains(String value) {
        return new SearchCondition(this, Id3Predicate.CONTAINS, value);
    }
}
