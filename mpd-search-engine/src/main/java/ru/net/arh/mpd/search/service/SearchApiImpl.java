package ru.net.arh.mpd.search.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import ru.net.arh.mpd.search.api.SearchApi;
import ru.net.arh.mpd.search.model.*;
import ru.net.arh.mpd.search.util.ConditionUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@PropertySources({
        @PropertySource("classpath:application.yaml")
        ,
        @PropertySource(value = "file:./custom/mpd-search.properties", ignoreResourceNotFound = true)
})
public class SearchApiImpl implements SearchApi {

    @Autowired
    private TreeService treeService;

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ESProperties esProperties;

    public SearchApiImpl(TreeService treeService, RestHighLevelClient client, ObjectMapper objectMapper, ESProperties esProperties) {
        this.treeService = treeService;
        this.client = client;
        this.objectMapper = objectMapper;
        this.esProperties = esProperties;
    }

    @Override
    public List<TreeItem> search(Condition searchCondition) {
        if (treeService.getItems() == null) {
            throw new RuntimeException("tree is null");
        }
        Predicate<TreeItem> predicate = ConditionUtil.getPredicate(searchCondition);
        return treeService.getItems().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    @Override
    public SearchResult search(SearchConditionNew searchCondition) {
        try {
            SearchRequest searchRequest = prepareSearchQuery(searchCondition);
            SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
            return mapToSearchResult(searchCondition, search);
        } catch (IOException e) {
            throw new RuntimeException("todo");
        }
    }

    private SearchRequest prepareSearchQuery(SearchConditionNew searchCondition) {
        QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(searchCondition.getSearchString()
                , searchCondition.getSearchPlaces()
                        .stream()
                        .map(SearchPlaces::getEsFieldName)
                        .toArray(String[]::new))
                .type(MultiMatchQueryBuilder.Type.BOOL_PREFIX);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                .query(queryBuilder)
                .from(searchCondition.getFrom())
                .size(searchCondition.getSize());
        return new SearchRequest(esProperties.getESindex())
                .source(sourceBuilder);
    }

    private SearchResult mapToSearchResult(SearchConditionNew searchCondition, SearchResponse search) {
        SearchResult result = new SearchResult();
        result.setFrom(searchCondition.getFrom());
        result.setSize(searchCondition.getSize());
        result.setItems(Arrays.stream(search.getHits().getHits())
                .map(this::toTreeItem)
                .collect(Collectors.toList()));
        result.setHasMore(search.getHits().getTotalHits().value > searchCondition.getFrom() + search.getHits().getHits().length);
        result.setTotalCount(search.getHits().getTotalHits().value);
        return result;
    }

    private TreeItem toTreeItem(SearchHit documentFields) {
        String json = documentFields.getSourceAsString();
        try {
            return objectMapper.readValue(json, TreeItem.class);
        } catch (IOException e) {
            return null;
        }
    }

}
