package ru.net.arh.mpd.search.api;

import org.springframework.beans.factory.annotation.Autowired;
import ru.net.arh.mpd.search.model.Condition;
import ru.net.arh.mpd.search.model.TreeItem;
import ru.net.arh.mpd.search.service.TreeService;
import ru.net.arh.mpd.search.util.ConditionUtil;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SearchApiImpl implements SearchApi {

    @Autowired
    private TreeService treeService;

    //@Autowired
    public SearchApiImpl(TreeService treeService) {
        this.treeService = treeService;
    }

    @Override
    public List<TreeItem> search(Condition searchCondition) {
        if (treeService.getTree() == null) {
            throw new RuntimeException("tree is null");
        }
        Predicate<TreeItem> predicate = ConditionUtil.getPredicate(searchCondition);
        return stream(treeService.getTree())
                .filter(treeItem -> treeItem.getFile() != null)
                .filter(predicate)
                .collect(Collectors.toList());
    }

    private Stream<TreeItem> stream(TreeItem treeItem) {
        if (treeItem.getFile() != null) {
            return Stream.of(treeItem);
        } else {
            return treeItem.getChildren().stream()
                    .map(child -> stream(child))
                    .reduce(Stream.of(treeItem), Stream::concat);
        }
    }
}
