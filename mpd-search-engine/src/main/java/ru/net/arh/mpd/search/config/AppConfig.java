package ru.net.arh.mpd.search.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.net.arh.mpd.search.api.SearchApi;
import ru.net.arh.mpd.search.service.SearchApiImpl;
import ru.net.arh.mpd.search.service.TreeService;

@Configuration
@EnableScheduling
@ComponentScan("ru.net.arh.mpd")
public class AppConfig {

    @Bean(name="/search")
    HttpInvokerServiceExporter search(TreeService treeService) {
        HttpInvokerServiceExporter exporter = new HttpInvokerServiceExporter();
        exporter.setService(new SearchApiImpl(treeService));
        exporter.setServiceInterface(SearchApi.class);
        return exporter;
    }

}
