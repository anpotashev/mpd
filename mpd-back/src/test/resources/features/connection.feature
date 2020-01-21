#language:ru
Функционал: установка соединения

  Сценарий: проверка и смена состояния соединения
#    Дано соеднинение с mpd разорвано
    И клиент устанавливает соединение
    И клиент подписывается на
      | /topic/connection | /user/queue/reply |
    Когда клиент отправляет пустой запрос по адресу /mpd/connectionState
    Тогда в течение 10 секунд получает ответ из очереди /user/queue/reply и сохраняет его значение по ключу "состояние подключения"
    И "состояние подключения" - false
    Когда клиент отправляет "true" по адресу /mpd/connectionState/change
    Тогда в течение 10 секунд получает ответ из очереди /topic/connection и сохраняет его значение по ключу "состояние подключения"
    И "состояние подключения" - true



#  "/topic/connection"
#  "/topic/playlist"
#  "/topic/status"
#  "/topic/tree"
#  "/topic/songTime"
#  "/topic/output"
#  "/topic/storedPlaylists"
#  "/topic/fullTree"