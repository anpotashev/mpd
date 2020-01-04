package ru.net.arh.mpd.search.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class SearchConditionNew implements Serializable {
    private String searchString;
    private List<SearchPlaces> searchPlaces;
    private int from;
    private int size;
}
