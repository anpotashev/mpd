package ru.net.arh.mpd.model.sockjs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class MpdSockJsError {

    private String type;
    private String msg;
}