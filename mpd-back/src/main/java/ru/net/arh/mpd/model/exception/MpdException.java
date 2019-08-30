package ru.net.arh.mpd.model.exception;

import org.slf4j.helpers.MessageFormatter;

public class MpdException extends RuntimeException {

    public MpdException(String message) {
        super(message);
    }

    public MpdException(String message, Object... args) {
        super(MessageFormatter.arrayFormat(message, args).getMessage());
    }
}