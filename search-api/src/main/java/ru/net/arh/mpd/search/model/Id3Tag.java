package ru.net.arh.mpd.search.model;

import java.util.function.Function;

public enum Id3Tag {
    ARTIST(s -> s.getArtist()),
    ALBUM(s -> s.getAlbum()),
    TITLE(s -> s.getTitle()),
    ALBUM_ARTIST(s -> s.getAlbumArtist()),
    GENRE(s -> s.getGenre())
    ;

    public String getAttributeValue(TreeItem treeItem) {
        return this.function.apply(treeItem);
    }

    private Function<TreeItem, String> function;

    Id3Tag(Function<TreeItem, String> function) {
        this.function = function;
    }
    public Condition contains(String value) {
        return new SearchCondition(this, Id3Predicate.CONTAINS, value);
    }

    public Condition startWith(String value) {
        return new SearchCondition(this, Id3Predicate.START_WITH, value);
    }

    public Condition regex(String value) { return new SearchCondition(this, Id3Predicate.REGEX, value); }
}
