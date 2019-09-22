package ru.net.arh.mpd.connection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
@PropertySources({
        @PropertySource("classpath:/config/application.yaml")
        ,
        @PropertySource(value = "file:./custom/mpd.properties", ignoreResourceNotFound = true)
})
public class ConnectionSettings {

    @Value("${mpdserver.host:${mpdserver.defaultHost}}")
    private String host;
    @Value("${mpdserver.port:${mpdserver.defaultPort}}")
    private Integer port;
    @Value("${mpdserver.password:${mpdserver.defaultPassword}}")
    private String password = "";

}
