package ru.net.arh.mpd.search.util;

import org.junit.Test;
import ru.net.arh.mpd.search.model.TreeItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class TreeItemUtilTest {

    AtomicInteger fileCounter = new AtomicInteger(0);
    class TreeItem4Test extends TreeItem {
        String expectedPath;
    }

    @Test
    public void setPathConvertToListAndRemoveDirsTest() {
        TreeItem root = getTreeItem();
        List<TreeItem> items = TreeItemUtil.setPathConvertToListAndRemoveDirs(root);
        assertEquals(items.size(), fileCounter.get());
        items.forEach(item -> assertEquals(item.getPath(), ((TreeItem4Test)item).expectedPath));
    }

    private TreeItem getTreeItem() {
        TreeItem root = new TreeItem();
        root.setDirectory("");
        root.setChildren(constructNTreeItem(3, ""));
        return root;
    }

    private List<TreeItem> constructNTreeItem(int n, String currentPath) {
        List<TreeItem> result = new ArrayList<>();
        List<TreeItem> files = IntStream.range(3, new Random().nextInt(10)).mapToObj(value -> {
            TreeItem4Test file = new TreeItem4Test();
            file.setFile("file_ " + value);
            file.expectedPath = currentPath.length()>0 ? currentPath + "/" + file.getFile() : file.getFile();
            fileCounter.incrementAndGet();
            return file;
        }).collect(Collectors.toList());
        List<TreeItem> dirs = IntStream.range(0, n).mapToObj(value -> {
            TreeItem dir = new TreeItem();
            dir.setDirectory("dir_" + value);
            dir.setChildren(constructNTreeItem(n - 1, currentPath.length()>0 ? currentPath + "/" + dir.getDirectory() : dir.getDirectory()));
            return dir;
        }).collect(Collectors.toList());
        result.addAll(files);
        result.addAll(dirs);
        return result;
    }
}