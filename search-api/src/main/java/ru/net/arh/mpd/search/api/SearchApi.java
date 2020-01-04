package ru.net.arh.mpd.search.api;

import ru.net.arh.mpd.search.model.Condition;
import ru.net.arh.mpd.search.model.SearchConditionNew;
import ru.net.arh.mpd.search.model.SearchResult;
import ru.net.arh.mpd.search.model.TreeItem;

import java.util.List;

public interface SearchApi {

    List<TreeItem> search(Condition searchCondition);
    SearchResult search(SearchConditionNew searchCondition);
}
