package ru.net.arh.mpd.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import ru.net.arh.mpd.model.MpdErrorType;
import ru.net.arh.mpd.model.outputs.MpdOutput;
import ru.net.arh.mpd.model.sockjs.ResponseType;
import ru.net.arh.mpd.model.sockjs.SockJsResponse;
import ru.net.arh.mpd.services.outputs.OutputsService;

import java.util.List;

import static ru.net.arh.mpd.web.MpdWsController.REPLY_QUEUE;

@Controller
public class MpdOutputController {
    @Autowired
    private OutputsService outputsService;

    /**
     * Запрос списка устройств вывода звука
     */
    @MpdErrorType(type = ResponseType.OUTPUT)
    @MessageMapping("/outputs")
    @SendToUser(REPLY_QUEUE)
    public SockJsResponse<List<MpdOutput>> playlist() {
        return new SockJsResponse<>(ResponseType.OUTPUT, outputsService.outputs());
    }

    /**
     * включить/отключить воспроизведение на указанное устройство вывода звука
     */
    @MessageMapping("/output/change")
    @MpdErrorType(type = ResponseType.OUTPUT)
    public void saveOutput(MpdOutput output) {
        outputsService.save(output);
    }

}
