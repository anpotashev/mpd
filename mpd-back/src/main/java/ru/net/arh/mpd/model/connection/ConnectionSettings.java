package ru.net.arh.mpd.model.connection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class ConnectionSettings {

    private String host = "127.0.0.1";
    private Integer port = 6600;
    private String password = "";

}