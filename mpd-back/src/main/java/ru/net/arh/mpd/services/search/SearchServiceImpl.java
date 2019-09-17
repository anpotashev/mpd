package ru.net.arh.mpd.services.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.net.arh.mpd.search.api.SearchApi;
import ru.net.arh.mpd.search.model.Condition;
import ru.net.arh.mpd.search.model.Id3Tag;
import ru.net.arh.mpd.search.model.SearchCondition;
import ru.net.arh.mpd.search.model.TreeItem;

import java.util.List;

import static ru.net.arh.mpd.search.model.Condition.and;
import static ru.net.arh.mpd.search.model.Condition.or;
import static ru.net.arh.mpd.search.model.Condition.not;
import static ru.net.arh.mpd.search.model.Id3Tag.ALBUM;
import static ru.net.arh.mpd.search.model.Id3Tag.ARTIST;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchApi searchApi;

    @Override
    public List<TreeItem> search(Condition condition) {
        return searchApi.search(condition);
    }
}
