package ru.net.arh.mpd.cache;

import lombok.Getter;

public enum CacheNames {
    PLAYLIST(Constants.PLAYLIST), TREE(Constants.TREE), FULL_TREE(Constants.FULL_TREE), STORED_PLAYLIST(Constants.STORED_PLAYLIST), STATUS(
            Constants.STATUS), OUTPUT(Constants.OUTPUT);

    @Getter
    final String cacheName;

    CacheNames(String cacheName) {
        this.cacheName = cacheName;
    }

    public static class Constants {
        public static final String PLAYLIST = "playlist";
        public static final String TREE = "tree";
        public static final String FULL_TREE = "full_tree";
        public static final String STORED_PLAYLIST = "stored_playlist";
        public static final String STATUS = "status";
        public static final String OUTPUT = "output";
    }

}
