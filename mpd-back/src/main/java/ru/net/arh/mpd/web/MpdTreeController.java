package ru.net.arh.mpd.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import ru.net.arh.mpd.model.MpdErrorType;
import ru.net.arh.mpd.model.sockjs.ResponseType;
import ru.net.arh.mpd.model.sockjs.SockJsResponse;
import ru.net.arh.mpd.model.tree.TreeItem;
import ru.net.arh.mpd.services.tree.MpdTreeService;

import static ru.net.arh.mpd.web.MpdWsController.REPLY_QUEUE;

@Controller
public class MpdTreeController {

    @Autowired
    private MpdTreeService treeService;

    /**
     * Запрос дерева музыкальной коллекции
     */
    @MessageMapping("/tree")
    @MpdErrorType(type = ResponseType.TREE)
    @SendToUser(REPLY_QUEUE)
    public SockJsResponse<TreeItem> tree() {
        return new SockJsResponse<>(ResponseType.TREE, treeService.tree());
    }

    /**
     * Запрос дерева музыкальной коллекции (детальной информации)
     */
    @MessageMapping("/fullTree")
    @MpdErrorType(type = ResponseType.FULL_TREE)
    @SendToUser(REPLY_QUEUE)
    public SockJsResponse<TreeItem> fullTree() {
        return new SockJsResponse<>(ResponseType.FULL_TREE, treeService.fullTree());
    }

    /**
     * Запрос дерева музыкальной коллекции (детальной информации)
     */
    @MessageMapping("/updateDb")
    @MpdErrorType(type = ResponseType.UPDATE_DB)
    @SendToUser(REPLY_QUEUE)
    public void update(String path) {
        treeService.update(path);
    }
}
