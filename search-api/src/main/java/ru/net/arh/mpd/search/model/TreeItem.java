package ru.net.arh.mpd.search.model;

import java.util.ArrayList;
import java.util.List;

public class TreeItem {

    private boolean showChildren = false;
    private TreeItem parent;
    private List<TreeItem> children = new ArrayList();
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
