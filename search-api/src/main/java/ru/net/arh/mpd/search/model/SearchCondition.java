package ru.net.arh.mpd.search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SearchCondition extends Condition {

    private Id3Tag id3Tag;
    private Id3Predicate operation;
    private String value;

    @Override
    public String toString() {
        return id3Tag + " " + operation + " " + value;
    }
}
