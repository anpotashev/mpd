package ru.net.arh.mpd.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import ru.net.arh.mpd.model.MpdErrorType;
import ru.net.arh.mpd.model.playlist.AddRequest;
import ru.net.arh.mpd.model.playlist.Playlist;
import ru.net.arh.mpd.model.sockjs.ResponseType;
import ru.net.arh.mpd.model.sockjs.SockJsResponse;
import ru.net.arh.mpd.services.playlist.PlaylistService;

import static ru.net.arh.mpd.web.MpdWsController.REPLY_QUEUE;

@Controller
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
    public void add(AddRequest request) {
        if (request.getPos() == null) {
            playlistService.add(request.getPath());
        } else {
            playlistService.addToPos(request.getPath(), request.getPos());
        }
    }


    @MessageMapping("/playlist/addFile")
    @MpdErrorType(type = ResponseType.PLAYLIST_ADD_FILE)
    public void addFile(AddRequest request) {
        if (request.getPos() == null) {
            playlistService.addFile(request.getPath());
        } else {
            playlistService.addFileToPos(request.getPath(), request.getPos());
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
    @MpdErrorType(type = ResponseType.PLAYLIST_CLEAR)
    public void shuffle() {
        playlistService.shuffle();
    }


}
