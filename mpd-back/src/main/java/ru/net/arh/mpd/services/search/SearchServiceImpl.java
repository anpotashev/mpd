package ru.net.arh.mpd.services.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.net.arh.mpd.search.api.SearchApi;
import ru.net.arh.mpd.search.model.Condition;
import ru.net.arh.mpd.search.model.TreeItem;

import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchApi searchApi;

    @Override
    public List<TreeItem> search(Condition condition) {
        return searchApi.search(condition);
    }
}
