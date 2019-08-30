package ru.net.arh.mpd.services.setting;

public interface SettingService {

    void random(boolean value);

    void repeat(boolean value);

    void single(boolean value);

    void consume(boolean value);
}
