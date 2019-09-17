package ru.net.arh.mpd.search.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TreeItem {
    private List<TreeItem> children;
    private String file;
    private String directory;
    private String time;
    private String artist;
    private String albumArtist;
    private String title;
    private String album;
    private String track;
    private String date;
    private String genre;
    private String path;
}
