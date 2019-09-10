package ru.net.arh.mpd.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import ru.net.arh.mpd.model.MpdErrorType;
import ru.net.arh.mpd.model.playlist.Playlist;
import ru.net.arh.mpd.model.sockjs.ResponseType;
import ru.net.arh.mpd.model.sockjs.SockJsResponse;
import ru.net.arh.mpd.services.playlist.PlaylistService;

import java.util.List;

import static ru.net.arh.mpd.web.MpdWsController.REPLY_QUEUE;

@Controller
public class MpdStoredPlaylistController {
    @Autowired
    private PlaylistService service;

    /**
     * Запрос списка сохраненных плейлистов
     */
    @MpdErrorType(type = ResponseType.STORED_PLAYLISTS)
    @MessageMapping("/storedPlaylist/list")
    @SendToUser(REPLY_QUEUE)
    public SockJsResponse<List<Playlist>> storedPlaylists() {
        return new SockJsResponse<>(ResponseType.STORED_PLAYLISTS, service.storedPlaylists());
    }


    /**
     * Запрос детальной информации о сохраненном плейлисте по его имени
     */
    @MpdErrorType(type = ResponseType.STORED_PLAYLIST)
    @MessageMapping("/storedPlaylist/info")
    @SendToUser(REPLY_QUEUE)
    public SockJsResponse<Playlist> storedPlaylist(String name) {
        return new SockJsResponse<>(ResponseType.STORED_PLAYLISTS, service.storedPlaylist(name));
    }


    /**
     * todo реализовать сохранение текущего плейлиста в списке stored_playlists
     * удаление, переименование и.т.п
     */
}
