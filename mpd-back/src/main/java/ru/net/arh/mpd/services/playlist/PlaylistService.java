package ru.net.arh.mpd.services.playlist;

import ru.net.arh.mpd.model.playlist.Playlist;
import ru.net.arh.mpd.model.playlist.PlaylistItem;

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
     * Добавить объект path в текущий плейлист.
     *
     * @param path
     */
    void add(String path);

    /**
     * Добавить объект path в текущий плейлист на указанную позицию.
     *
     * @param path
     * @param position
     */
    void addToPos(String path, int position);

    /**
     * Удалить объект item из текущего плейлиста.
     *
     * @param item
     */
    void delete(PlaylistItem item);

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
     * Перемешать текущий плейлист.
     */
    void shuffle();

    /**
     * Перемешать текущий плейлист с позиции start по позицию end.
     *
     * @param start
     * @param end
     */
    void shuffle(int start, int end);

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
     * Добавить файл
     *
     * @param value
     */
    void addFile(String value);

    /**
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
     */
    void addStored(String value);

    /**
     * @param value
     * @param position
     */
    void addStored(String value, Integer position);
}
