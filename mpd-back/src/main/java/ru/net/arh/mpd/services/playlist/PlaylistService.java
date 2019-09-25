package ru.net.arh.mpd.services.playlist;

import ru.net.arh.mpd.model.playlist.Playlist;
import ru.net.arh.mpd.model.playlist.PlaylistItem;
import ru.net.arh.mpd.search.model.TreeItem;

import java.util.List;

/**
 * Сервис работы с плейлистом.
 */
public interface PlaylistService {

    /**
     * Текущий плейлист.
     *
     * @return
     */
    Playlist playlist();

    /**
     * Очистить текущий плейлист.
     */
    void clear();

    /**
     * Добавить объект path в текущий плейлист на указанную позицию или в конец, если она null
     *
     * @param path
     * @param position
     */
    void addToPos(String path, Integer position);

    /**
     * Удалить объект item из текущего плейлиста.
     *
     * @param item
     */
    void delete(PlaylistItem item);

    /**
     * Удалить объект item из текущего плейлиста по его позиции.
     */
    void delete(int pos);

    /**
     * Переместить трек с позиции from на позицию to.
     *
     * @param from
     * @param to
     */
    void move(int from, int to);

    /**
     * Переместить треки начиная с позиции fromStart по позицию fromEnd на позицию to.
     *
     * @param fromStart
     * @param fromEnd
     * @param to
     */
    void move(int fromStart, int fromEnd, int to);

    /**
     * Перемешать текущий плейлист с позиции start по позицию end. Если они null, то перемешивает весь плейлист
     *
     * @param start
     * @param end
     */
    void shuffle(Integer start, Integer end);

    /**
     * Получить список сохраненных плейлистов.
     *
     * @return
     */
    List<Playlist> storedPlaylists();

    /**
     * Получить содержимое сохраненного плейлиста по имени.
     *
     * @param name
     * @return
     */
    Playlist storedPlaylist(String name);

    /**
     * Добавить файл в текущий плейлист в указанную позицию (в конец если она null)
     *
     * @param value
     * @param position
     */
    void addFileToPos(String value, Integer position);

    /**
     * @param value
     */
    void load(String value);

    /**
     * @param value
     * @param position
     */
    void addStored(String value, Integer position);

    /**
     * Удалить сохраненный плейлист по его имени
     *
     * @param name имя плейлиста
     */
    void rmStored(String name);

    /**
     * Сохранить текущий плейлист в списке сохраненных
     *
     * @param name - имя нового плейлиста
     */
    void saveStored(String name);

    /**
     * переименовать сохраненный плейлист
     *
     * @param oldName - старое имя
     * @param newName - новое имя
     */
    void renameStored(String oldName, String newName);

    /**
     * Добавить в текущий плейлист лист треков в указанную позицию.
     * Если не задана, то добавляются в конец
     *
     * @param items
     */
    void addAll(List<TreeItem> items, Integer pos);

}
