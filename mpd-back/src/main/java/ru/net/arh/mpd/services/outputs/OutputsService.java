package ru.net.arh.mpd.services.outputs;

import ru.net.arh.mpd.model.outputs.MpdOutput;

import java.util.List;

/**
 * Сервис работы с "устройствами и способами вывода звука".
 */
public interface OutputsService {

    /**
     * Получить список всех устройств вывода.
     *
     * @return
     */
    List<MpdOutput> outputs();

    /**
     * Включить или выключить устройство.
     *
     * @param output
     */
    void save(MpdOutput output);
}
