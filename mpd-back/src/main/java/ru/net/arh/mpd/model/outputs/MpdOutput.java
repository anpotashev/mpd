package ru.net.arh.mpd.model.outputs;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.net.arh.mpd.model.MpdAnswer;

@Getter
@Setter
@MpdAnswer(preffix = "outputid:")
@EqualsAndHashCode
public class MpdOutput {
    @MpdAnswer(preffix = "outputid:")
    private int id;
    @MpdAnswer(preffix = "outputname:")
    private String name;
    @MpdAnswer(preffix = "outputenabled:")
    private boolean enabled;

}
