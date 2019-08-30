package ru.net.arh.mpd.connection;

/**
 * Периодические "пинги" сервера для поддержания соединения.
 */
public interface PingService {

  /**
   * Отправить команду ping
   */
  void ping();
}
