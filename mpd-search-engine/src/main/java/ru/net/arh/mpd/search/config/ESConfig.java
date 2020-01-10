package ru.net.arh.mpd.search.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
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
//    @Value("${elasticsearch.username}")
//    private String username;
//    @Value("${elasticsearch.password}")
//    private String password;

    @Bean(destroyMethod = "close")
    public RestHighLevelClient client() {
//        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//        credentialsProvider.setCredentials(AuthScope.ANY,new UsernamePasswordCredentials("username", "password"));
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost(elasticsearchHost, elasticsearchPort, elasticsearchProtocol))
//                        .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider))
        );
        return client;
    }


}