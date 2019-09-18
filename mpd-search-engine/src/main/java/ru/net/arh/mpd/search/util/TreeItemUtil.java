package ru.net.arh.mpd.search.util;

import ru.net.arh.mpd.search.model.TreeItem;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TreeItemUtil {

    public static List<TreeItem> setPathConvertToListAndRemoveDirs(TreeItem rootItem) {
        setPaths(rootItem);
        return stream(rootItem)
                .filter(treeItem -> treeItem.getFile() != null)
                .collect(Collectors.toList());
    }

    private static void setPaths(TreeItem rootItem) {
        rootItem.setDirectory("");
        setPaths(rootItem, "");
    }

    private static void setPaths(TreeItem treeItem, String parentPath) {
        if (treeItem.getFile() != null) {
            treeItem.setPath(constructPath(parentPath, treeItem.getFile()));
            return;
        }
        treeItem.setPath(constructPath(parentPath, treeItem.getDirectory()));
        treeItem.getChildren().forEach(treeItem1 -> setPaths(treeItem1, treeItem.getPath()));
    }

    private static String constructPath(String parentPath, String path) {
        return parentPath.length()>0
                ? parentPath + "/" + path
                : path;
    }

    private static Stream<TreeItem> stream(TreeItem treeItem) {
        if (treeItem.getFile() != null) {
            return Stream.of(treeItem);
        } else {
            return treeItem.getChildren().stream()
                    .map(child -> stream(child))
                    .reduce(Stream.of(treeItem), Stream::concat);
        }
    }
}
