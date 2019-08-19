package ru.net.arh.mpd.model.sockjs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SockJsResponse<P> {
    private ResponseType type;
    private P payload;

}
