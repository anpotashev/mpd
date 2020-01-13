package ru.net.arh.mpd.search.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.net.arh.mpd.search.api.SearchApi;
import ru.net.arh.mpd.search.model.Condition;
import ru.net.arh.mpd.search.model.TreeItem;
import ru.net.arh.mpd.search.util.ConditionUtil;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.when;
import static ru.net.arh.mpd.search.model.Id3Tag.ARTIST;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ConditionUtil.class)
public class SearchApiImplTest {
    @Mock
    private TreeService treeService;
    @Mock
    private RestHighLevelClient restHighLevelClient;
    @Mock
    private ObjectMapper objectMapper;
    private SearchApi searchApi;
    @Mock
    private ESProperties properties;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        PowerMockito.mockStatic(ConditionUtil.class);
        when(ConditionUtil.getPredicate(any(Condition.class)))
                .thenReturn(treeItem -> Integer.parseInt(treeItem.getTitle()) % 2 == 0);
        searchApi = new SearchApiImpl(treeService, restHighLevelClient, objectMapper, properties);
    }

    @Test
    public void search() {
        List<TreeItem> source = getTreeItems();
        when(treeService.getItems()).thenReturn(source);
        Condition condition = ARTIST.contains("ASDF");
        List<TreeItem> result = searchApi.search(condition);
        result.forEach(treeItem -> assertTrue(ConditionUtil.getPredicate(condition).test(treeItem)));
        source.removeAll(result);
        source.forEach(treeItem -> assertFalse(ConditionUtil.getPredicate(condition).test(treeItem)));
    }

    private List<TreeItem> getTreeItems() {
        return IntStream.range(0, 100).mapToObj(operand -> {
            TreeItem item = new TreeItem();
            item.setTitle("" + operand);
            return item;
        }).collect(Collectors.toList());
    }
}