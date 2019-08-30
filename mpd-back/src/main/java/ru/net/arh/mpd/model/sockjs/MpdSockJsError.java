package ru.net.arh.mpd.model.sockjs;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MpdSockJsError {

    private String type;
    private String msg;
}