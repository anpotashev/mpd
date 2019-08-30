package ru.net.arh.mpd.connection.idle;

import ru.net.arh.mpd.model.events.MpdEvent;

/**
 * Слежка за событиями.
 *
 */
public interface IdleService {

  /**
   * Начать отправку команды idle
   */
  void startListeningIdle();

  /**
   * Прекратить отправку команды idle
   */
  void stopListeningIdle();
}