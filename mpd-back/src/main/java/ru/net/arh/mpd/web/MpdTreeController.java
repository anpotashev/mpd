package ru.net.arh.mpd.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import ru.net.arh.mpd.model.MpdErrorType;
import ru.net.arh.mpd.model.sockjs.ResponseType;
import ru.net.arh.mpd.model.sockjs.SockJsResponse;
import ru.net.arh.mpd.model.tree.TreeItem;
import ru.net.arh.mpd.services.tree.MpdTreeService;
import ru.net.arh.mpd.validation.MapKeys;

import java.util.Map;

import static ru.net.arh.mpd.web.MpdWsController.REPLY_QUEUE;

@Controller
@Validated
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
     * Запрос обновления дерева музыкальной коллекции
     */
    @MessageMapping("/updateDb")
    @MpdErrorType(type = ResponseType.UPDATE_DB)
    @SendToUser(REPLY_QUEUE)
    public void update(@MapKeys(keys = "path") Map<String, String> map) {
        treeService.update(map.get("path"));
    }
}
