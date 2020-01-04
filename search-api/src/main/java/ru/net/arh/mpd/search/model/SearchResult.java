package ru.net.arh.mpd.search.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class SearchResult implements Serializable {

    private List<TreeItem> items;
    private int from;
    private int size;
    private boolean hasMore;
    private long totalCount;
}
