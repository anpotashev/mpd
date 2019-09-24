package ru.net.arh.mpd.util;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.Setter;
import ru.net.arh.mpd.model.MpdAnswer;
import ru.net.arh.mpd.model.status.MpdSongTime;

import java.util.List;

@Getter
@Setter
@MpdAnswer(preffix = MpdPojo4Test.INT_FIELD_PREFIX)
public class MpdPojo4Test {

    public static final String INT_FIELD_PREFIX="intField:";
    private static final String STRING_FIELD_PREFIX="stringField:";
    private static final String BOOLEAN_FIELD_PREFIX="booleanField:";
    private static final String SONG_TIME_PREFIX = "songTimeField:";

    @MpdAnswer(preffix = INT_FIELD_PREFIX)
    private int intField;

    @MpdAnswer(preffix = STRING_FIELD_PREFIX)
    private String stringFild;

    @MpdAnswer(preffix = BOOLEAN_FIELD_PREFIX)
    private boolean booleanField;

    @MpdAnswer(preffix = SONG_TIME_PREFIX)
    private MpdSongTime songTime;


    public List<String> stringList() {
        return ImmutableList.of(
                INT_FIELD_PREFIX + intField,
                STRING_FIELD_PREFIX + stringFild,
                BOOLEAN_FIELD_PREFIX + (booleanField ? "1" : "0"),
                SONG_TIME_PREFIX + songTime.getCurrent() + ":" + songTime.getFull()
        );
    }

}
