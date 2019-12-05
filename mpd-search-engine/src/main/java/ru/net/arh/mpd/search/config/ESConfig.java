package ru.net.arh.mpd.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
        @PropertySource("classpath:application.yaml")
        ,
        @PropertySource(value = "file:./custom/mpd-search.properties", ignoreResourceNotFound = true)
})
public class ESConfig {

    @Value("${elasticsearch.host}")
    private String elasticsearchHost;
    @Value("${elasticsearch.port}")
    private int elasticsearchPort;
    @Value("${elasticsearch.protocol}")
    private String elasticsearchProtocol;

    @Bean(destroyMethod = "close")
    public RestHighLevelClient client() {

        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost(elasticsearchHost, elasticsearchPort, elasticsearchProtocol)));
        return client;

    }


}