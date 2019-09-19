package ru.net.arh.mpd.search.service;

import ru.net.arh.mpd.search.model.TreeItem;

import java.util.List;

/**
 * Сервис, целью которого является возвращать актуальную коллекцию треков.
 */
public interface TreeService {

    List<TreeItem> getItems();
}
