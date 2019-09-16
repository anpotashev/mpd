package ru.net.arh.mpd.search.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;
import ru.net.arh.mpd.search.api.SearchApi;
import ru.net.arh.mpd.search.api.SearchApiImpl;

@Configuration
public class RpcConfig {
    @Bean(name="/search")
    HttpInvokerServiceExporter search() {
        HttpInvokerServiceExporter exporter = new HttpInvokerServiceExporter();
        exporter.setService(new SearchApiImpl());
        exporter.setServiceInterface(SearchApi.class);
        return exporter;
    }

}
