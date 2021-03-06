package ru.net.arh.mpd.services.search;

import ru.net.arh.mpd.search.model.Condition;
import ru.net.arh.mpd.search.model.SearchConditionNew;
import ru.net.arh.mpd.search.model.SearchResult;
import ru.net.arh.mpd.search.model.TreeItem;

import java.util.List;

public interface SearchService {
    List<TreeItem> search(Condition condition);
    SearchResult search(SearchConditionNew searchConditionNew);
}
