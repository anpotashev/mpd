package ru.net.arh.mpd.search.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.client.RestHighLevelClient;
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
    HttpInvokerServiceExporter search(TreeService treeService, RestHighLevelClient client, ObjectMapper objectMapper) {
        HttpInvokerServiceExporter exporter = new HttpInvokerServiceExporter();
        exporter.setService(new SearchApiImpl(treeService, client, objectMapper));
        exporter.setServiceInterface(SearchApi.class);
        return exporter;
    }

}
