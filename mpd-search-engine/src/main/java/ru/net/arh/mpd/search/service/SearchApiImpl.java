package ru.net.arh.mpd.search.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.net.arh.mpd.search.api.SearchApi;
import ru.net.arh.mpd.search.model.Condition;
import ru.net.arh.mpd.search.model.TreeItem;
import ru.net.arh.mpd.search.service.TreeService;
import ru.net.arh.mpd.search.util.ConditionUtil;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SearchApiImpl implements SearchApi {

    @Autowired
    private TreeService treeService;

    public SearchApiImpl(TreeService treeService) {
        this.treeService = treeService;
    }

    @Override
    public List<TreeItem> search(Condition searchCondition) {
        if (treeService.getItems() == null) {
            throw new RuntimeException("tree is null");
        }
        Predicate<TreeItem> predicate = ConditionUtil.getPredicate(searchCondition);
        return treeService.getItems().stream()
                .filter(treeItem -> treeItem.getFile() != null)
                .filter(predicate)
                .collect(Collectors.toList());
    }

}
