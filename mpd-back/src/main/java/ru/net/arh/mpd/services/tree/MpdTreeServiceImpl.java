package ru.net.arh.mpd.services.tree;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import ru.net.arh.mpd.aop.ThrowIfNotConnected;
import ru.net.arh.mpd.cache.CacheNames;
import ru.net.arh.mpd.connection.ConnectionService;
import ru.net.arh.mpd.model.MpdCommand;
import ru.net.arh.mpd.model.MpdCommand.Command;
import ru.net.arh.mpd.model.MpdIdleType;
import ru.net.arh.mpd.model.events.MpdEventType;
import ru.net.arh.mpd.model.events.MpdIdleEventMethod;
import ru.net.arh.mpd.model.events.MpdIldeEventClass;
import ru.net.arh.mpd.model.tree.TreeItem;
import ru.net.arh.mpd.util.MpdAnswersParser;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@MpdIldeEventClass
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
        List<String> list = connectionService.sendCommand(new MpdCommand(Command.LISTALL));
        TreeItem root = mpdListAllParser.parse(list);
        return root;
    }

    @Override
    @ThrowIfNotConnected
    @Cacheable(cacheNames = CacheNames.Constants.FULL_TREE)
    @MpdIdleEventMethod(types = {MpdIdleType.DATABASE}, eventType = MpdEventType.FULL_TREE_CHANGED)
    public TreeItem fullTree() {
        List<String> list = connectionService.sendCommand(new MpdCommand(Command.LISTALLINFO));
        TreeItem root = mpdListAllParser.parse(list);
        return root;
    }

    public TreeItem root() {
        return rootItem;
    }

    @Cacheable(cacheNames = CacheNames.Constants.TREE)
    @ThrowIfNotConnected
    public List<TreeItem> children(final String path) {
        MpdCommand mpdCommand = new MpdCommand(Command.LSINFO);
        mpdCommand.addParam(path);
        List<String> list = connectionService.sendCommand(mpdCommand);
        List<TreeItem> children = MpdAnswersParser.parseAll(TreeItem.class, list);
        return children;
    }

    @Override
    @ThrowIfNotConnected
    public void update(String path) {
        MpdCommand command = new MpdCommand(Command.UPDATE);
        command.addParam(path);
        connectionService.sendCommand(command);
    }

    @Override
    @ThrowIfNotConnected
    public void add(String path) {
        MpdCommand command = new MpdCommand(Command.LISTALL);
        command.addParam(path);
        List<TreeItem> treeItems = MpdAnswersParser
                .parseAll(TreeItem.class, connectionService.sendCommand(command));
        List<MpdCommand> commands = treeItems.stream()
                .filter(item -> item.getFile() != null)
                .map(item -> new MpdCommand(Command.ADD, item.getFile()))
                .collect(Collectors.toList());
        connectionService.sendCommands(commands);
    }

}
