package ru.net.arh.mpd.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import ru.net.arh.mpd.model.MpdErrorType;
import ru.net.arh.mpd.model.sockjs.ResponseType;
import ru.net.arh.mpd.model.sockjs.SockJsResponse;
import ru.net.arh.mpd.search.model.Condition;
import ru.net.arh.mpd.search.model.TreeItem;
import ru.net.arh.mpd.services.search.SearchService;

import java.util.List;

import static ru.net.arh.mpd.web.MpdWsController.REPLY_QUEUE;

@Controller
public class MpdSearchController {

    @Autowired
    private SearchService searchService;

    @MessageMapping("/search")
    @SendToUser(REPLY_QUEUE)
    @MpdErrorType(type = ResponseType.SEARCH)
    public <T extends Condition> SockJsResponse<List<TreeItem>> search(T condition) {
        return new SockJsResponse<>(ResponseType.SEARCH, searchService.search(condition));
    }
}
