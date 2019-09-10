package ru.net.arh.mpd.model.playlist;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO для добавления элемента path в указанную позицию в плейлисте.
 */
@Getter
@Setter
@NoArgsConstructor
public class AddRequest {
    private String path;
    private Integer pos;
}
