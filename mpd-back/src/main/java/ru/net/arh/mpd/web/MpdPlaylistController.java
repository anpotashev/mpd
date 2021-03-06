package ru.net.arh.mpd.web;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import ru.net.arh.mpd.model.MpdErrorType;
import ru.net.arh.mpd.model.playlist.Playlist;
import ru.net.arh.mpd.model.sockjs.ResponseType;
import ru.net.arh.mpd.model.sockjs.SockJsResponse;
import ru.net.arh.mpd.search.model.Condition;
import ru.net.arh.mpd.services.playlist.PlaylistService;
import ru.net.arh.mpd.services.search.SearchService;
import ru.net.arh.mpd.validation.MapKeys;

import java.util.Map;

import static ru.net.arh.mpd.web.MpdWsController.REPLY_QUEUE;

@Controller
@Validated
public class MpdPlaylistController {

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private SearchService searchService;
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
        playlistService.addToPos((String)map.get("path"), (Integer) map.getOrDefault("pos", null));
    }


    @MessageMapping("/playlist/addFile")
    @MpdErrorType(type = ResponseType.PLAYLIST_ADD_FILE)
    public void addFile(@MapKeys(keys = {"path"}) Map<String, Object> map) {
        playlistService.addFileToPos((String)map.get("path"), (Integer) map.getOrDefault("pos", null));
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
        playlistService.shuffle(map.getOrDefault("from", null), map.getOrDefault("to", null));
    }

    @MessageMapping("/playlist/remove")
    @MpdErrorType(type = ResponseType.PLAYLIST_RM)
    public void rm(@MapKeys(keys = {"pos"}) Map<String, Integer> map) {
        playlistService.delete(map.get("pos"));
    }


    @MessageMapping("/playlist/move")
    @MpdErrorType(type = ResponseType.PLAYLIST_MOVE)
    public void move(@MapKeys(keys = {"from", "to"}) Map<String, Integer> map) {
        playlistService.move(map.get("from"), map.get("to"));
    }

    @MessageMapping("playlist/add/search")
    @MpdErrorType(type = ResponseType.PLAYLIST_ADD_SEARCH)
    public void addSearchToPlaylist(AddSearchRequest searchRequest) {
        playlistService.addAll(searchService.search(searchRequest.getCondition()), searchRequest.getPos());
    }

    @Getter
    @Setter
    public static class AddSearchRequest {
        private Condition condition;
        private Integer pos;
    }

}
