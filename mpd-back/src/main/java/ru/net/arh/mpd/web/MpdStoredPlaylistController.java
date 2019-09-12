package ru.net.arh.mpd.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import ru.net.arh.mpd.model.MpdErrorType;
import ru.net.arh.mpd.model.playlist.Playlist;
import ru.net.arh.mpd.model.sockjs.ResponseType;
import ru.net.arh.mpd.model.sockjs.SockJsResponse;
import ru.net.arh.mpd.services.playlist.PlaylistService;
import ru.net.arh.mpd.validation.MapKeys;

import java.util.List;
import java.util.Map;

import static ru.net.arh.mpd.web.MpdWsController.REPLY_QUEUE;

@Controller
@Validated
public class MpdStoredPlaylistController {
    @Autowired
    private PlaylistService service;

    /**
     * Запрос списка сохраненных плейлистов
     */
    @MpdErrorType(type = ResponseType.STORED_PLAYLISTS)
    @MessageMapping("/storedPlaylist")
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

    @MpdErrorType(type = ResponseType.STORED_PLAYLIST_LOAD)
    @MessageMapping("/storedPlaylist/load")
    public void loadStored(@MapKeys(keys = {"storedPlaylist"}) Map<String, Object> map) {
        service.clear();
        service.load((String) map.get("storedPlaylist"));
    }

    @MpdErrorType(type = ResponseType.STORED_PLAYLIST_ADD)
    @MessageMapping("/storedPlaylist/add")
    public void addStored(@MapKeys(keys = {"storedPlaylist"}) Map<String, Object> map) {
        if (map.containsKey("pos")) {
            service.addStored((String) map.get("storedPlaylist"), (Integer) map.get("pos"));
        } else {
            service.addStored((String) map.get("storedPlaylist"));
        }
    }

    @MpdErrorType(type = ResponseType.STORED_PLAYLIST_RM)
    @MessageMapping("/storedPlaylist/rm")
    public void rmStored(@MapKeys(keys = {"name"}) Map<String, String> map) {
        service.rmStored(map.get("name"));
    }

    @MpdErrorType(type = ResponseType.STORED_PLAYLIST_SAVE)
    @MessageMapping("/storedPlaylist/save")
    public void saveStored(@MapKeys(keys = {"name"}) Map<String, String> map) {
        service.saveStored(map.get("name"));
    }

    @MpdErrorType(type = ResponseType.STORED_PLAYLIST_RENAME)
    @MessageMapping("/storedPlaylist/rename")
    public void renameStored(@MapKeys(keys = {"oldName", "newName"}) Map<String, String> map) {
        service.renameStored(map.get("oldName"), map.get("newName"));
    }
}
