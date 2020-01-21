#language:ru
Функционал: работа с устройствами вывода

  Сценарий: получение списка устройств вывода; их включение/отключение
  Клиент устанавливает соединение с бэк-частью.
  Запрашивает список устройств вывода при отстуствии коннекта м/ду бэк-частью и mpd-сервисом. Получает ошибку
  Устанавливает соединение м/ду бэк-частью и mpd-сервисом и повторно запрашивает список устройств вывода.
  Получает список.
  Отправляет команду на отключение одного из устройств.
  Получает уведомление об изменении и обновленный список устройств.
  Повторяет предыдущий шаг но уже на включение.

    Дано клиент устанавливает соединение
    И клиент подписывается на
      | /topic/output | /user/queue/reply | /user/queue/error | /topic/connection |
    Когда клиент отправляет по адресу /mpd/output/change объект с полями:
      | поле    | значение       |
      | id      | 0              |
      | name    | Http streaming |
      | enabled | false          |
    Тогда в течение 10 секунд получает ответ из очереди /user/queue/error и сохраняет его значение по ключу "ошибки"
    И "ошибки" содержит поля:
      | поле | значение      |
      | type | OUTPUT_FAILED |
      | msg  | not connected |
    Когда клиент отправляет "true" по адресу /mpd/connectionState/change
    Тогда в течение 10 секунд получает ответ из очереди /topic/connection и сохраняет его значение по ключу "состояние подключения"
    И "состояние подключения" - true
    Когда клиент отправляет пустой запрос по адресу /mpd/outputs
    Тогда в течение 10 секунд получает ответ из очереди /user/queue/reply и сохраняет его значение по ключу "устройства вывода"
    И "устройства вывода" содержит элемент с полями:
      | поле    | значение       |
      | id      | 0              |
      | name    | Http streaming |
      | enabled | true           |
    Когда клиент отправляет по адресу /mpd/output/change объект с полями:
      | поле    | значение       |
      | id      | 0              |
      | name    | Http streaming |
      | enabled | false          |
    Тогда в течение 10 секунд получает ответ из очереди /topic/output и сохраняет его значение по ключу "устройства вывода"
    И "устройства вывода" содержит элемент с полями:
      | поле    | значение       |
      | id      | 0              |
      | name    | Http streaming |
      | enabled | false          |
    Когда клиент отправляет по адресу /mpd/output/change объект с полями:
      | поле    | значение       |
      | id      | 0              |
      | name    | Http streaming |
      | enabled | true           |
    Тогда в течение 10 секунд получает ответ из очереди /topic/output и сохраняет его значение по ключу "устройства вывода"
    И "устройства вывода" содержит элемент с полями:
      | поле    | значение       |
      | id      | 0              |
      | name    | Http streaming |
      | enabled | true           |