package ru.net.arh.mpd.services.tree;

import ru.net.arh.mpd.model.tree.TreeItem;

import java.util.List;

public interface MpdListAllParser {

    TreeItem parse(List<String> list);
}
