package ru.net.arh.mpd.services.tree;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.net.arh.mpd.model.tree.TreeItem;
import ru.net.arh.mpd.util.MpdAnswersParser;

import java.util.List;

/**
 * Класс для парсинга ответов mpd-сервера возвращающих дерево коллекции
 */
@Slf4j
@Service
public class MpdListAllParserImpl implements MpdListAllParser {

    private static void updateName(TreeItem item, String newName) {
        if (item.isLeaf()) {
            item.setFile(newName);
        } else {
            item.setDirectory(newName);
        }
    }

    private static TreeItem findParent(String checkedItemPath, final TreeItem lastParentItem) {
        TreeItem p = lastParentItem;
        while (!checkedItemPath.startsWith(p.getPath())) {
            p = p.getParent();
        }
        return p;
    }

    @Override
    public TreeItem parse(List<String> list) {
        TreeItem root = new TreeItem();
        root.setShowChildren(true);
        final TreeItem[] lastItem = {root};

        List<TreeItem> treeItems = MpdAnswersParser.parseAll(TreeItem.class, list);
        treeItems.forEach(item -> {
            item.setShowChildren(true);
            String path = (item.isLeaf()) ? item.getFile() : item.getDirectory();
            TreeItem parentItem = findParent(path, lastItem[0]);
            item.setParent(parentItem);
            parentItem.getChildren().add(item);
            path = parentItem.isRoot() ? path : path.substring(parentItem.getPath().length() + 1);
            updateName(item, path);
            if (!item.isLeaf()) {
                lastItem[0] = item;
            }

        });
        return root;
    }
}
