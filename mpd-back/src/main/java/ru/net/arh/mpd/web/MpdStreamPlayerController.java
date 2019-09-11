package ru.net.arh.mpd.web;

import static ru.net.arh.mpd.web.MpdWsController.REPLY_QUEUE;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import ru.net.arh.mpd.model.sockjs.ResponseType;
import ru.net.arh.mpd.model.sockjs.SockJsResponse;
import ru.net.arh.mpd.model.tree.TreeItem;

@Controller
@PropertySources({
    @PropertySource("classpath:/config/application.yaml")
    ,
    @PropertySource(value = "file:${MPD_CONFIG}/mpd.properties", ignoreResourceNotFound = true)
})
public class MpdStreamPlayerController {

  @Value("${mpdserver.streamUrl:${mpdserver.defaultStreamUrl}}")
  private String streamUrl;

  @MessageMapping("/streamPlayer")
  @SendToUser(REPLY_QUEUE)
  public SockJsResponse<String> streamUrl() {
    return new SockJsResponse<>(ResponseType.STREAM_URL, streamUrl);
  }
}
