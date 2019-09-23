package ru.net.arh.mpd.search.util;

import org.junit.Test;
import ru.net.arh.mpd.search.model.Condition;
import ru.net.arh.mpd.search.model.PredicateCondition;
import ru.net.arh.mpd.search.model.TreeItem;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;
import static ru.net.arh.mpd.search.model.Id3Tag.*;
import static ru.net.arh.mpd.search.model.Id3Tag.GENRE;

public class ConditionUtilTest {

    @Test
    public void testSearchCondition() {
        trueSearchConditions().forEach(c -> assertTrue(ConditionUtil.getPredicate(c).test(treeItem())));
        truePredicateCondition().forEach(c -> assertTrue(ConditionUtil.getPredicate(c).test(treeItem())));
        falseSearchConditions().forEach(c -> assertFalse(ConditionUtil.getPredicate(c).test(treeItem())));
        falsePredicateCondition().forEach(c -> assertFalse(ConditionUtil.getPredicate(c).test(treeItem())));
    }

    private List<Condition> truePredicateCondition() {
        return Arrays.asList(
                PredicateCondition.not(getRandomFalseCondition()),
                PredicateCondition.and(getRandomTrueCondition(), getRandomTrueCondition()),
                PredicateCondition.or(getRandomFalseCondition(), getRandomTrueCondition()),
                PredicateCondition.not(PredicateCondition.and(getRandomTrueCondition(), getRandomFalseCondition())),
                PredicateCondition.not(PredicateCondition.or(getRandomFalseCondition(), getRandomFalseCondition()))
        );
    }
    private List<Condition> falsePredicateCondition() {
        return Arrays.asList(
                PredicateCondition.not(getRandomTrueCondition()),
                PredicateCondition.and(getRandomFalseCondition(), getRandomTrueCondition()),
                PredicateCondition.or(getRandomFalseCondition(), getRandomFalseCondition()),
                PredicateCondition.not(PredicateCondition.and(getRandomTrueCondition(), getRandomTrueCondition())),
                PredicateCondition.not(PredicateCondition.or(getRandomFalseCondition(), getRandomTrueCondition()))
        );
    }

    private Condition getRandomFalseCondition() {
        List<Condition> conditions = falseSearchConditions();
        return conditions.get(new Random().nextInt(conditions.size()));
    }

    private Condition getRandomTrueCondition() {
        List<Condition> conditions = trueSearchConditions();
        return conditions.get(new Random().nextInt(conditions.size()));
    }

    private List<Condition> trueSearchConditions() {
        return Arrays.asList(
                ARTIST.contains("RTIS")
                , ARTIST.startWith("ART")
                , ARTIST.regex(".*(AAA|ART).*T")
                , ALBUM.contains("LBU")
                , ALBUM.startWith("ALB")
                , ALBUM.regex("AL.*M")
                , TITLE.contains("ITLE")
                , TITLE.startWith("TIT")
                , TITLE.regex(".*(AAA|IT).*E")
                , ALBUM_ARTIST.contains("ARTIST")
                , ALBUM_ARTIST.startWith("ALBUM")
                , ALBUM_ARTIST.regex(".*(AAA|M_ART).*T")
                , GENRE.startWith("GEN")
                , GENRE.contains("ENR")
                , GENRE.regex(".(EEE|ENR).")
        );
    }
    private List<Condition> falseSearchConditions() {
        return Arrays.asList(
                ARTIST.contains("___ARTIST"),
                ARTIST.startWith("___ART"),
                ARTIST.regex(".*(AAA|ART).*TTTT"),
                ALBUM.contains("___ALBUM"),
                ALBUM.startWith("A__LB"),
                ALBUM.regex("AL.*MMMMM"),
                TITLE.contains("___TITLE"),
                TITLE.startWith("__TIT"),
                TITLE.regex(".*(AAA|IT).*TTTT"),
                ALBUM_ARTIST.contains("___ARTIST"),
                ALBUM_ARTIST.startWith("ARTIST"),
                ALBUM_ARTIST.regex(".*(AAA|M_ART).*TTTT"),
                GENRE.startWith("_GEN"),
                GENRE.contains("_ENR"),
                GENRE.regex(".(EEE|ENR_)."));
    }

    private TreeItem treeItem() {
        TreeItem treeItem = new TreeItem();
        treeItem.setArtist("ARTIST");
        treeItem.setAlbum("ALBUM");
        treeItem.setAlbumArtist("ALBUM_ARTIST");
        treeItem.setTitle("TITLE");
        treeItem.setGenre("GENRE");
        return treeItem;
    }
}
