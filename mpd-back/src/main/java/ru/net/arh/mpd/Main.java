package ru.net.arh.mpd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@PropertySources({
        @PropertySource("classpath:/config/application.yaml")
        ,
        @PropertySource(value = "file:${MPD_CONFIG}/custom/mpd.properties", ignoreResourceNotFound = false)
})
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}

