package ru.net.arh.mpd.services.tree;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.net.arh.mpd.aop.ThrowIfNotConnected;
import ru.net.arh.mpd.cache.CacheNames;
import ru.net.arh.mpd.connection.ConnectionService;
import ru.net.arh.mpd.model.MpdIdleType;
import ru.net.arh.mpd.model.commands.MpdCommand;
import ru.net.arh.mpd.model.commands.MpdCommandBuilder;
import ru.net.arh.mpd.model.commands.MpdCommandBuilder.Command;
import ru.net.arh.mpd.model.events.MpdEventType;
import ru.net.arh.mpd.model.events.MpdIdleEventMethod;
import ru.net.arh.mpd.model.tree.TreeItem;
import ru.net.arh.mpd.util.MpdAnswersParser;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MpdTreeServiceImpl implements MpdTreeService {

    private TreeItem rootItem;
    @Autowired
    private ConnectionService connectionService;
    @Autowired
    private MpdListAllParser mpdListAllParser;

    @PostConstruct
    public void postConstruct() {
        rootItem = new TreeItem();
        rootItem.setDirectory("");
    }

    @Override
    @ThrowIfNotConnected
    @Cacheable(cacheNames = CacheNames.Constants.TREE)
    @MpdIdleEventMethod(types = {MpdIdleType.DATABASE}, eventType = MpdEventType.TREE_CHANGED)
    public TreeItem tree() {
        List<String> list = connectionService.sendCommand(MpdCommandBuilder.of(Command.LISTALL));
        return mpdListAllParser.parse(list);
    }

    @Override
    @ThrowIfNotConnected
    @Cacheable(cacheNames = CacheNames.Constants.FULL_TREE)
    @MpdIdleEventMethod(types = {MpdIdleType.DATABASE}, eventType = MpdEventType.FULL_TREE_CHANGED)
    public TreeItem fullTree() {
        List<String> list = connectionService.sendCommand(MpdCommandBuilder.of(Command.LISTALLINFO));
        return mpdListAllParser.parse(list);
    }

    public TreeItem root() {
        return rootItem;
    }

    @Cacheable(cacheNames = CacheNames.Constants.TREE)
    @ThrowIfNotConnected
    public List<TreeItem> children(final String path) {
        List<String> list = connectionService.sendCommand(MpdCommandBuilder.of(Command.LSINFO).add(path));
        return MpdAnswersParser.parseAll(TreeItem.class, list);
    }

    @Override
    @ThrowIfNotConnected
    public void update(String path) {
        connectionService.sendCommand(MpdCommandBuilder.of(Command.UPDATE).add(path));
    }

    @Override
    @ThrowIfNotConnected
    public void add(String path) {
        List<TreeItem> treeItems = MpdAnswersParser
                .parseAll(TreeItem.class, connectionService.sendCommand(MpdCommandBuilder.of(Command.LISTALL).add(path)));
        List<MpdCommand> commands = treeItems.stream()
                .filter(item -> item.getFile() != null)
                .map(item -> MpdCommandBuilder.of(Command.ADD).add(item.getFile()))
                .collect(Collectors.toList());
        connectionService.sendCommands(commands);
    }

}
