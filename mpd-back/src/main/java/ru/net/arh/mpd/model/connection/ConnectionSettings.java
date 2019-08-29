package ru.net.arh.mpd.model.connection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
        @PropertySource(value = "file:${MPD_CONFIG}/custom/mpd.properties", ignoreResourceNotFound = true)
})
public class ConnectionSettings {

    @Value("${mpdserver.host:${mpdserver.defaulthost}}")
    private String host;
    @Value("${mpdserver.port:${mpdserver.defaultport}}")
    private Integer port;
    @Value("${mpdserver.password:${mpdserver.defaultpassword}}")
    private String password = "";

}