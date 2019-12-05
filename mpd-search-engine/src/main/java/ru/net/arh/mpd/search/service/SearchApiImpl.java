package ru.net.arh.mpd.search.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import ru.net.arh.mpd.search.api.SearchApi;
import ru.net.arh.mpd.search.model.Condition;
import ru.net.arh.mpd.search.model.TreeItem;
import ru.net.arh.mpd.search.util.ConditionUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Collections.EMPTY_LIST;

public class SearchApiImpl implements SearchApi {

    @Autowired
    private TreeService treeService;

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private ObjectMapper objectMapper;

    public SearchApiImpl(TreeService treeService, RestHighLevelClient client, ObjectMapper objectMapper) {
        this.treeService = treeService;
        this.client = client;
        this.objectMapper = objectMapper;
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
    public List<TreeItem> search(String searchString) {
        try {
            QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(searchString);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                    .query(queryBuilder);
            SearchRequest searchRequest = new SearchRequest("mpd")
                    .source(sourceBuilder);
            SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
            return Arrays.stream(search.getHits().getHits())
                    .map(this::toTreeItem)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return EMPTY_LIST;
        }
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
