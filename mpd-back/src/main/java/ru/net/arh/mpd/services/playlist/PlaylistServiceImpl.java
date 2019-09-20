package ru.net.arh.mpd.services.playlist;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.net.arh.mpd.aop.ThrowIfNotConnected;
import ru.net.arh.mpd.cache.CacheNames;
import ru.net.arh.mpd.connection.ConnectionService;
import ru.net.arh.mpd.model.MpdCommand;
import ru.net.arh.mpd.model.MpdCommand.Command;
import ru.net.arh.mpd.model.MpdIdleType;
import ru.net.arh.mpd.model.events.MpdEventType;
import ru.net.arh.mpd.model.events.MpdIdleEventMethod;
import ru.net.arh.mpd.model.playlist.Playlist;
import ru.net.arh.mpd.model.playlist.PlaylistItem;
import ru.net.arh.mpd.model.tree.TreeItem;
import ru.net.arh.mpd.util.MpdAnswersParser;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
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
        return new Playlist(MpdAnswersParser.parseAll(PlaylistItem.class, connectionService.sendCommand(Command.PLAYLIST_INFO.build())));
    }

    @Override
    @ThrowIfNotConnected
    public void clear() {
        connectionService.sendCommand(Command.CLEAR.build());
    }

    @Override
    @ThrowIfNotConnected
    public void addToPos(String path, Integer position) {
        MpdCommand command = Command.LISTALL.build(path);
        List<String> paths = MpdAnswersParser
                .parseAll(TreeItem.class, connectionService.sendCommand(command))
                .stream()
                .filter(TreeItem::isLeaf)
                .map(TreeItem::getFile)
                .collect(Collectors.toList());
        addToPlaylist(paths, position);
    }

    @Override
    @ThrowIfNotConnected
    public void delete(PlaylistItem item) {
        connectionService.sendCommand(Command.DELETE.build(item.getId() + ""));
    }

    @Override
    @ThrowIfNotConnected
    public void delete(int pos) {
        connectionService.sendCommand(Command.DELETE.build(pos + ""));
    }

    @Override
    @ThrowIfNotConnected
    public void move(int from, int to) {
        connectionService.sendCommand(Command.MOVE.build(from + "", to + ""));
    }

    @Override
    @ThrowIfNotConnected
    public void move(int fromStart, int fromEnd, int to) {
        connectionService.sendCommand(Command.MOVE.build(fromStart + ":" + fromEnd, to + ""));
    }

    @Override
    @ThrowIfNotConnected
    public void shuffle(Integer start, Integer end) {
        MpdCommand mpdCommand = (start != null && end != null)
                ? Command.SHUFFLE.build(start + "", end + "")
                :Command.SHUFFLE.build();
        connectionService.sendCommand(mpdCommand);
    }

    @Override
    @ThrowIfNotConnected
    @Cacheable(cacheNames = CacheNames.Constants.STORED_PLAYLIST)
    @MpdIdleEventMethod(eventType = MpdEventType.STORED_PLAYLISTS, types = {MpdIdleType.STORED_PLAYLIST})
    public List<Playlist> storedPlaylists() {
        List<String> list = connectionService.sendCommand(Command.LISTPLAYLISTS.build());
        List<Playlist> playlists = MpdAnswersParser.parseAll(Playlist.class, list);
        playlists.forEach(playlist -> playlist.setPlaylistItems(playlistService.storedPlaylist(playlist.getName()).getPlaylistItems()));
        return playlists;
    }

    @Override
    @ThrowIfNotConnected
    @Cacheable(cacheNames = CacheNames.Constants.STORED_PLAYLIST)
    public Playlist storedPlaylist(String name) {
        MpdCommand command = Command.LISTPLAYLIST_INFO.build(name);
        List<String> list = connectionService.sendCommand(command);
        return new Playlist(
                MpdAnswersParser.parseAll(PlaylistItem.class, list)
        );
    }

    @Override
    @ThrowIfNotConnected
    public void addFileToPos(String path, Integer position) {
        MpdCommand command = position == null
                ? Command.ADD.build(path)
                : Command.ADD_ID.build(path, position.toString());
        connectionService.sendCommand(command);
    }

    @Override
    @ThrowIfNotConnected
    public void load(String name) {
        connectionService.sendCommand(Command.LOAD.build(name));
    }

    @Override
    @ThrowIfNotConnected
    public void addStored(String value, Integer position) {
        if (position == null) {
            position = playlistService.playlist().getPlaylistItems().size();
        }
        Playlist playlist = playlistService.storedPlaylist(value);
        final AtomicInteger currentPosition = new AtomicInteger(position);
        List<MpdCommand> commands = playlist.getPlaylistItems()
                .stream()
                .map(item -> Command.ADD_ID.build(item.getFile(),
                                            currentPosition.getAndIncrement() + ""
                ))
                .collect(Collectors.toList());
        connectionService.sendCommands(commands);
    }

    @Override
    @ThrowIfNotConnected
    public void rmStored(String name) {
        connectionService.sendCommand(Command.RM.build(name));
    }

    @Override
    @ThrowIfNotConnected
    public void saveStored(String name) {
        connectionService.sendCommand(Command.SAVE.build(name));
    }

    @Override
    @ThrowIfNotConnected
    public void renameStored(String oldName, String newName) {
        connectionService.sendCommand(Command.RENAME.build(oldName, newName));
    }

    @Override
    @ThrowIfNotConnected
    public void addAll(List<ru.net.arh.mpd.search.model.TreeItem> items, Integer pos) {
        this.addToPlaylist(items.stream().map(ru.net.arh.mpd.search.model.TreeItem::getPath).collect(Collectors.toList()), pos);
    }

    private void addToPlaylist(List<String> paths, Integer pos) {
        if (pos == null) {
            List<MpdCommand> commands = paths.stream()
                    .map(Command.ADD::build)
                    .collect(Collectors.toList());
            connectionService.sendCommands(commands);
            return;
        }
        final AtomicInteger currentPosition = new AtomicInteger(pos);
        List<MpdCommand> commands = paths.stream()
                .map(item -> Command.ADD_ID.build(item,
                        currentPosition.getAndIncrement() + ""
                ))
                .collect(Collectors.toList());
        connectionService.sendCommands(commands);
    }

}
