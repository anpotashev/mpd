package ru.net.arh.mpd.services.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.net.arh.mpd.cache.CacheNames;
import ru.net.arh.mpd.search.api.SearchApi;
import ru.net.arh.mpd.search.model.Condition;
import ru.net.arh.mpd.search.model.TreeItem;

import java.io.IOException;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchApi searchApi;

    @Override
    @Cacheable(cacheNames = CacheNames.Constants.FULL_TREE)
    public List<TreeItem> search(Condition condition) {
        return searchApi.search(condition);
    }

    @Override
    public List<TreeItem> search(String searchString) {
        return searchApi.search(searchString);
    }
}
