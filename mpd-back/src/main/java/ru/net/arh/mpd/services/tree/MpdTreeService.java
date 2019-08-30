package ru.net.arh.mpd.services.tree;

import ru.net.arh.mpd.model.tree.TreeItem;

import java.util.List;

/**
 * Сервис управляющий деревом музыкальной коллекции
 */
public interface MpdTreeService {

    /**
     * Запрос дерева
     */
    TreeItem tree();

    /**
     * Запрос дерева (детальная информация)
     */
    TreeItem fullTree();

    /**
     * Корень коллекции
     */
    TreeItem root();

    /**
     * Дочерние элементы
     */
    List<TreeItem> children(String path);

    /**
     * Запросить обновление коллекции
     *
     * @param path - путь к элементу в дереве. Можно получить вызвав метод @see ru.net.arh.mpd.model.tree.TreeItem#getPath
     */
    void update(String path);

    /**
     * Добавить элемент в текущий плейлист
     *
     * @param path - путь к элементу в дереве. Можно получить вызвав метод @see ru.net.arh.mpd.model.tree.TreeItem#getPath
     */
    void add(String path);
}
