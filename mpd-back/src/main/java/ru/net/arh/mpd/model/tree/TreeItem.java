package ru.net.arh.mpd.model.tree;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.net.arh.mpd.model.MpdAnswer;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Getter
@Setter
@NoArgsConstructor
@MpdAnswer(preffix = {"file:", "directory:"})
@JsonInclude(Include.NON_EMPTY)
public class TreeItem {

    @JsonIgnore
    private boolean showChildren = false;
    @JsonIgnore
    private TreeItem parent;
    @JsonIgnore
    private List<TreeItem> children = newArrayList();
    @MpdAnswer(preffix = "file:")
    private String file;
    @MpdAnswer(preffix = "directory:")
    private String directory;
    @MpdAnswer(preffix = "Time:")
    private String time;
    @MpdAnswer(preffix = "Artist:")
    private String artist;
    @MpdAnswer(preffix = "AlbumArtist:")
    private String albumArtist;
    @MpdAnswer(preffix = "Title:")
    private String title;
    @MpdAnswer(preffix = "Album:")
    private String album;
    @MpdAnswer(preffix = "Track:")
    private String track;
    @MpdAnswer(preffix = "Date:")
    private String date;
    @MpdAnswer(preffix = "Genre:")
    private String genre;

    @JsonGetter("parent")
    public TreeItem parent() {
      if (showChildren) {
        return null;
      }
        return parent;
    }

    @JsonGetter("children")
    public List<TreeItem> children() {
      if (!showChildren) {
        return null;
      }
        return children;
    }

    @JsonIgnore
    public boolean isLeaf() { return getFile() != null; }

    @JsonIgnore
    public boolean isRoot() { return getParent() == null; }

    @JsonIgnore
    public String getPath() {
        if (isRoot()) {
            return "";
        }
        return (getParent().isRoot() ? "" : getParent().getPath() + "/") +
               (isLeaf() ? getFile() : getDirectory());
    }
}
