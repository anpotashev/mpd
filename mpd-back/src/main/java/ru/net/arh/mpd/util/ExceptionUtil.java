package ru.net.arh.mpd.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.event.Level;
import org.slf4j.helpers.MessageFormatter;
import ru.net.arh.mpd.model.exception.MpdException;
import ru.net.arh.mpd.util.LogginUtil;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionUtil {

    public static MpdException logAndGetException(String messagePattern, Logger logger, Level level, Object...args) {
        String msg = MessageFormatter.arrayFormat(messagePattern, args).getMessage();
        return logAndGetException(msg, logger, level);
    }

    public static MpdException logAndGetException(String messagePattern, Logger logger, Object...args) {
        return logAndGetException(messagePattern, logger, Level.ERROR, args);
    }

    public static MpdException logAndGetException(String message, Logger logger) {
        return logAndGetException(message, logger, Level.ERROR);
    }

    public static MpdException logAndGetException(String message, Logger logger, Level level) {
        LogginUtil.log(logger, level, message);
        return getException(message);
    }

    public static MpdException getException(String message) {
        return new MpdException(message);
    }
}