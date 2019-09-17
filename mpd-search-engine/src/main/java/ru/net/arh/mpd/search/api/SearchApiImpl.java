package ru.net.arh.mpd.search.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.net.arh.mpd.search.model.Condition;
import ru.net.arh.mpd.search.model.TreeItem;
import ru.net.arh.mpd.search.service.TreeService;

import java.util.ArrayList;
import java.util.List;

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
        //пока просто возвращаю дерево, завернутое в лист
        ArrayList<TreeItem> result = new ArrayList<>();
        result.add(treeService.getTree());
        return result;
    }
}
