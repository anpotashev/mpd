package ru.net.arh.mpd.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import ru.net.arh.mpd.model.MpdErrorType;
import ru.net.arh.mpd.model.sockjs.ResponseType;
import ru.net.arh.mpd.model.sockjs.SockJsResponse;
import ru.net.arh.mpd.search.model.Condition;
import ru.net.arh.mpd.search.model.TreeItem;
import ru.net.arh.mpd.services.search.SearchService;

import java.util.List;

@Controller
public class MpdSearchController {

    @Autowired
    private SearchService searchService;

    @MessageMapping("/search")
    @MpdErrorType(type = ResponseType.SEARCH)
    public SockJsResponse<List<TreeItem>> search(Condition condition) {
        return new SockJsResponse<>(ResponseType.SEARCH, searchService.search(condition));
    }
}
