package ru.net.arh.mpd.services.playlist;

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
import ru.net.arh.mpd.model.playlist.Playlist;
import ru.net.arh.mpd.model.playlist.PlaylistItem;
import ru.net.arh.mpd.model.tree.TreeItem;
import ru.net.arh.mpd.util.MpdAnswersParser;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@MpdIldeEventClass
public class PlaylistServiceImpl implements PlaylistService {

    private final ConnectionService connectionService;

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    public PlaylistServiceImpl(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @Override
    @ThrowIfNotConnected
    @Cacheable(CacheNames.Constants.PLAYLIST)
    @MpdIdleEventMethod(types = {MpdIdleType.PLAYLIST}, eventType = MpdEventType.PLAYLIST_CHANGED)
    public Playlist playlist() {
        List<String> list = connectionService.sendCommand(
                new MpdCommand(Command.PLAYLIST_INFO)
        );
        return new Playlist(MpdAnswersParser.parseAll(PlaylistItem.class, list));
    }

    @Override
    @ThrowIfNotConnected
    public void clear() {
        connectionService.sendCommand(new MpdCommand(Command.CLEAR));
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

    @Override
    @ThrowIfNotConnected
    public void addToPos(String path, int position) {
        final AtomicInteger currentPosition = new AtomicInteger(position);
        MpdCommand command = new MpdCommand(Command.LISTALL);
        command.addParam(path);
        List<TreeItem> treeItems = MpdAnswersParser
                .parseAll(TreeItem.class, connectionService.sendCommand(command));
        List<MpdCommand> commands = treeItems.stream()
                .filter(item -> item.getFile() != null)
                .map(item -> new MpdCommand(Command.ADD_ID, item.getFile(),
                                            currentPosition.getAndIncrement() + ""
                ))
                .collect(Collectors.toList());
        connectionService.sendCommands(commands);
    }

    @Override
    @ThrowIfNotConnected
    public void delete(PlaylistItem item) {
        MpdCommand command = new MpdCommand(Command.DELETE);
        int id = item.getId();
        command.addParam(id + "");
        connectionService.sendCommand(command);
    }

    @Override
    @ThrowIfNotConnected
    public void move(int from, int to) {
        MpdCommand command = new MpdCommand(Command.MOVE);
        command.addParam(from + "");
        command.addParam(to + "");
        connectionService.sendCommand(command);
    }

    @Override
    @ThrowIfNotConnected
    public void move(int fromStart, int fromEnd, int to) {
        MpdCommand command = new MpdCommand(Command.MOVE);
        command.addParam(fromStart + ":" + fromEnd);
        command.addParam(to + "");
        connectionService.sendCommand(command);
    }

    @Override
    @ThrowIfNotConnected
    public void shuffle() {
        MpdCommand command = new MpdCommand(Command.SHUFFLE);
        connectionService.sendCommand(command);
    }

    @Override
    @ThrowIfNotConnected
    public void shuffle(int start, int end) {
        MpdCommand command = new MpdCommand(Command.SHUFFLE);
        command.addParam(start + "");
        command.addParam(end + "");
        connectionService.sendCommand(command);
    }

    @Override
    @ThrowIfNotConnected
    @Cacheable(cacheNames = CacheNames.Constants.STORED_PLAYLIST)
    @MpdIdleEventMethod(eventType = MpdEventType.STORED_PLAYLISTS, types = {MpdIdleType.STORED_PLAYLIST})
    public List<Playlist> storedPlaylists() {
        List<String> list = connectionService.sendCommand(new MpdCommand(Command.LISTPLAYLISTS));
        List<Playlist> playlists = MpdAnswersParser.parseAll(Playlist.class, list);
        playlists.forEach(playlist -> playlist.setPlaylistItems(playlistService.storedPlaylist(playlist.getName()).getPlaylistItems()));
        return playlists;
    }

    @Override
    @ThrowIfNotConnected
    @Cacheable(cacheNames = CacheNames.Constants.STORED_PLAYLIST)
    public Playlist storedPlaylist(String name) {
        MpdCommand command = new MpdCommand(Command.LISTPLAYLIST_INFO);
        command.addParam(name);
        List<String> list = connectionService.sendCommand(command);
        return new Playlist(
                MpdAnswersParser.parseAll(PlaylistItem.class, list)
        );
    }

    @Override
    @ThrowIfNotConnected
    public void addFile(String path) {
        MpdCommand command = new MpdCommand(Command.ADD, path);
        connectionService.sendCommand(command);
    }

    @Override
    @ThrowIfNotConnected
    public void addFileToPos(String path, Integer position) {
        MpdCommand command = new MpdCommand(Command.ADD_ID, path, position.toString());
        connectionService.sendCommand(command);
    }

    @Override
    @ThrowIfNotConnected
    public void load(String name) {
        connectionService.sendCommand(new MpdCommand(Command.LOAD, name));
    }

    @Override
    @ThrowIfNotConnected
    public void addStored(String value) {
        playlistService.addStored(value, playlistService.playlist().getPlaylistItems().size());
    }

    @Override
    @ThrowIfNotConnected
    public void addStored(String value, Integer position) {
        Playlist playlist = playlistService.storedPlaylist(value);
        final AtomicInteger currentPosition = new AtomicInteger(position);
        List<MpdCommand> commands = playlist.getPlaylistItems()
                .stream()
                .map(item -> new MpdCommand(Command.ADD_ID, item.getFile(),
                                            currentPosition.getAndIncrement() + ""
                ))
                .collect(Collectors.toList());
        connectionService.sendCommands(commands);
    }

    @Override
    @ThrowIfNotConnected
    public void rmStored(String name) {
        connectionService.sendCommand(new MpdCommand(Command.RM, name));
    }

    @Override
    @ThrowIfNotConnected
    public void saveStored(String name) {
        connectionService.sendCommand(new MpdCommand(Command.SAVE, name));
    }

    @Override
    @ThrowIfNotConnected
    public void renameStored(String oldName, String newName) {
        connectionService.sendCommand(new MpdCommand(Command.RENAME, oldName, newName));
    }


}
