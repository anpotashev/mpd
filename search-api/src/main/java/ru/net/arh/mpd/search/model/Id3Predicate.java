package ru.net.arh.mpd.search.model;

import lombok.Getter;

import java.util.function.BiFunction;

public enum Id3Predicate {

    CONTAINS ((s, s2) -> s!=null && s.toLowerCase().contains(s2.toLowerCase())),
    START_WITH ((s, s2) -> s!=null && s.toLowerCase().startsWith(s2.toLowerCase())),
    REGEX ((s, s2) -> s!=null && s.matches(s2))
    ;

    @Getter
    private BiFunction<String, String, Boolean> function;

    Id3Predicate(BiFunction<String, String, Boolean> function) {
        this.function = function;
    }
}
