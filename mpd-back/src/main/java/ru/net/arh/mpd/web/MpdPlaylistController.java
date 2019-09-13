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

import java.util.Map;

import static ru.net.arh.mpd.web.MpdWsController.REPLY_QUEUE;

@Controller
@Validated
public class MpdPlaylistController {

    @Autowired
    private PlaylistService playlistService;

    /**
     * Запрос текущего плейлиста
     */
    @MpdErrorType(type = ResponseType.PLAYLIST)
    @MessageMapping("/playlist")
    @SendToUser(REPLY_QUEUE)
    public SockJsResponse<Playlist> playlist() {
        return new SockJsResponse<>(ResponseType.PLAYLIST, playlistService.playlist());
    }

    @MessageMapping("/playlist/add")
    @MpdErrorType(type = ResponseType.PLAYLIST_ADD)
    public void add(@MapKeys(keys = {"path"}) Map<String, Object> map) {
        if (map.get("pos") == null) {
            playlistService.add((String)map.get("path"));
        } else {
            playlistService.addToPos((String)map.get("path"), (Integer) map.get("pos"));
        }
    }


    @MessageMapping("/playlist/addFile")
    @MpdErrorType(type = ResponseType.PLAYLIST_ADD_FILE)
    public void addFile(@MapKeys(keys = {"path"}) Map<String, Object> map) {
        if (map.containsKey("pos")) {
            playlistService.addFileToPos((String)map.get("path"), (Integer) map.get("pos"));
        } else {
            playlistService.addFile((String) map.get("path"));
        }
    }

    /**
     * Очистить текущий плейлист
     */
    @MessageMapping("/playlist/clear")
    @MpdErrorType(type = ResponseType.PLAYLIST_CLEAR)
    public void playlistClear() {
        playlistService.clear();
    }


    /**
     * Перемешать текущий плейлист
     */
    @MessageMapping("/playlist/shuffle")
    @MpdErrorType(type = ResponseType.PLAYLIST_SHUFFLE)
    public void shuffle(Map<String, Integer> map) {
        if (map.containsKey("from") && map.containsKey("to")) {
            playlistService.shuffle(map.get("from"), map.get("to"));
        } else {
            playlistService.shuffle();
        }
    }


}
