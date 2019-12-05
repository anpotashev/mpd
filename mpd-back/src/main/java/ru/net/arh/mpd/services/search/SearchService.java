package ru.net.arh.mpd.services.search;

import ru.net.arh.mpd.search.model.Condition;
import ru.net.arh.mpd.search.model.TreeItem;

import java.io.IOException;
import java.util.List;

public interface SearchService {
    List<TreeItem> search(Condition condition);
    List<TreeItem> search(String searchString);
}
