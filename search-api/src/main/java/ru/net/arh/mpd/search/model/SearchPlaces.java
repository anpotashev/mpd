package ru.net.arh.mpd.search.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public enum SearchPlaces {
    @JsonProperty("file") FILE("file"),
    @JsonProperty("path") PATH("path"),
    @JsonProperty("title") TITLE("title"),
    @JsonProperty("artist") ARTIST("artist"),
    @JsonProperty("album") ALBUM("album"),
    @JsonProperty("albumArtist") ALBUM_ARTIST("albumArtist"),
    @JsonProperty("genre") GENRE("genre");

    @Getter
    private final String esFieldName;

    SearchPlaces(String esFieldName) {
        this.esFieldName = esFieldName;
    }
}