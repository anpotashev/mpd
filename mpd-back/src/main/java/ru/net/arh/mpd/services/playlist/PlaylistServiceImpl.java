package ru.net.arh.mpd.services.playlist;

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
        return new Playlist(MpdAnswersParser.parseAll(PlaylistItem.class, connectionService.sendCommand(MpdCommandBuilder.of(Command.PLAYLIST_INFO))));
    }

    @Override
    @ThrowIfNotConnected
    public void clear() {
        connectionService.sendCommand(MpdCommandBuilder.of(Command.CLEAR));
    }

    @Override
    @ThrowIfNotConnected
    public void addToPos(String path, Integer position) {
        MpdCommand command = MpdCommandBuilder.of(Command.LISTALL).add(path);
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
        connectionService.sendCommand(MpdCommandBuilder.of(Command.DELETE).add(item.getId()));
    }

    @Override
    @ThrowIfNotConnected
    public void delete(int pos) {
        connectionService.sendCommand(MpdCommandBuilder.of(Command.DELETE).add(pos));
    }

    @Override
    @ThrowIfNotConnected
    public void move(int from, int to) {
        connectionService.sendCommand(MpdCommandBuilder.of(Command.MOVE).add(from, to));
    }

    @Override
    @ThrowIfNotConnected
    public void move(int fromStart, int fromEnd, int to) {
        connectionService.sendCommand(MpdCommandBuilder.of(Command.MOVE).add(fromStart + ":" + fromEnd, to + ""));
    }

    @Override
    @ThrowIfNotConnected
    public void shuffle(Integer start, Integer end) {
        MpdCommand mpdCommand = (start != null && end != null)
                ? MpdCommandBuilder.of(Command.SHUFFLE).add(start, end)
                : MpdCommandBuilder.of(Command.SHUFFLE);
        connectionService.sendCommand(mpdCommand);
    }

    @Override
    @ThrowIfNotConnected
    @Cacheable(cacheNames = CacheNames.Constants.STORED_PLAYLIST)
    @MpdIdleEventMethod(eventType = MpdEventType.STORED_PLAYLISTS, types = {MpdIdleType.STORED_PLAYLIST})
    public List<Playlist> storedPlaylists() {
        List<String> list = connectionService.sendCommand(MpdCommandBuilder.of(Command.LISTPLAYLISTS));
        List<Playlist> playlists = MpdAnswersParser.parseAll(Playlist.class, list);
        playlists.forEach(playlist -> playlist.setPlaylistItems(playlistService.storedPlaylist(playlist.getName()).getPlaylistItems()));
        return playlists;
    }

    @Override
    @ThrowIfNotConnected
    @Cacheable(cacheNames = CacheNames.Constants.STORED_PLAYLIST)
    public Playlist storedPlaylist(String name) {
        MpdCommand command = MpdCommandBuilder.of(Command.LISTPLAYLIST_INFO).add(name);
        List<String> list = connectionService.sendCommand(command);
        return new Playlist(
                MpdAnswersParser.parseAll(PlaylistItem.class, list)
        );
    }

    @Override
    @ThrowIfNotConnected
    public void addFileToPos(String path, Integer position) {
        MpdCommand command = position == null
                ? MpdCommandBuilder.of(Command.ADD).add(path)
                : MpdCommandBuilder.of(Command.ADD_ID).add(path, position.toString());
        connectionService.sendCommand(command);
    }

    @Override
    @ThrowIfNotConnected
    public void load(String name) {
        connectionService.sendCommand(MpdCommandBuilder.of(Command.LOAD).add(name));
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
                .map(item -> MpdCommandBuilder.of(Command.ADD_ID).add(item.getFile(),
                        currentPosition.getAndIncrement() + ""
                ))
                .collect(Collectors.toList());
        connectionService.sendCommands(commands);
    }

    @Override
    @ThrowIfNotConnected
    public void rmStored(String name) {
        connectionService.sendCommand(MpdCommandBuilder.of(Command.RM).add(name));
    }

    @Override
    @ThrowIfNotConnected
    public void saveStored(String name) {
        connectionService.sendCommand(MpdCommandBuilder.of(Command.SAVE).add(name));
    }

    @Override
    @ThrowIfNotConnected
    public void renameStored(String oldName, String newName) {
        connectionService.sendCommand(MpdCommandBuilder.of(Command.RENAME).add(oldName, newName));
    }

    @Override
    @ThrowIfNotConnected
    public void addAll(List<ru.net.arh.mpd.search.model.TreeItem> items, Integer pos) {
        this.addToPlaylist(items.stream().map(ru.net.arh.mpd.search.model.TreeItem::getPath).collect(Collectors.toList()), pos);
    }

    private void addToPlaylist(List<String> paths, Integer pos) {
        if (paths.isEmpty()) return;
        if (pos == null) {
            List<MpdCommand> commands = paths.stream()
                    .map(s -> MpdCommandBuilder.of(Command.ADD).add(s))
                    .collect(Collectors.toList());
            connectionService.sendCommands(commands);
            return;
        }
        final AtomicInteger currentPosition = new AtomicInteger(pos);
        List<MpdCommand> commands = paths.stream()
                .map(item -> MpdCommandBuilder.of(Command.ADD_ID).add(item,
                        currentPosition.getAndIncrement() + ""
                ))
                .collect(Collectors.toList());
        connectionService.sendCommands(commands);
    }

}
