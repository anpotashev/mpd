**MPD**

Проект представляет собой websocket API для взаимодействия с mpd-сервером.
Имеется фронтовая часть на typescript (react+redux).

Проект состоит из двух модулей: mpd-back и mpd-front

Описание пакетов в бэк-модуле:
 - ru.net.arh.mpd:
   - aop - аспекты
   - cache - кэш
   - config - spring-конфигурация
   - connection - внутренний API взаимодействия с mpd-сервером
   - events - сервисы обрабатывающие события (connect, disconnect, idle-события). 
   Результатом является отправка событий с обновленной информацией. Так же происходит очистка кэшей по необходимости.
   - model - модельные сущности
   - services - набор сервисов, содержащих логику взаимодействия м/ду клиентским API и внутренним АPI (connection)
   - stomp - содержит сервис учета подписчиков. Служит для того, чтобы при отсутствии "подписчиков" на события web-socket, при наступлении событий, не происходил запрос информации.
   Является необязательным, служит для снижения нагрузки.
   - util - содержит утильный класс по парсингу "сырых" ответов mpd-сервера в объекты/коллекции объектов
   - validation - валидация
   - web - контроллеры, хендлеры
   
Описание кастомных аннотаций:
  - @MpdAnswer - служит для парсинга ответов.
  - @ThrowIfNotConnected - вешается на методы сервисов. В случае если метод вызван, когда отсутствует соединение с mpd, сразу бросает исключение.
  - @MapKeys - вешается на параметры типа Map. Валидация мапы на предмет наличия в ней указанных в аннотации ключей.
  - @MpdIldeEventClass/@MpdIldeEventMethod - вешается на классы/методы сервисов. Помечает метод для вызова при наступлении соотвествующего события. 
  При наличии над этим методом аннотации @Cacheable, так же предварительно очищает указанные в ней кэши.
  - MpdErrorType - помечает методы контроллера. При получении Exception в ответ пользователю подставляется <тип запроса>_FAILED из этой аннотации.

**WebSocket API:**

Запрос:
```js
{
    <REQUEST>
}
```

Ответы об ошибках (отправляются в /user/queue/error):

```js
{ 
    type: <TYPE1>_FAILED, 
    msg: <текст ошибки>
}
```

Success ответ (отправляется в /user/queue/reply). 
Для сервисов, которые реагируют на idle-события, происходит отправка в topic без запроса со стороны пользователя.
```js
{
    type: <TYPE2>,
    payload: <RESPONSE>
}
```

RESPONSE, TYPE1 и TYPE2 описаны ниже для конкретных действий. 

Отсутсвие значения в таблице для TYPE1, означает, что запрос не предполагает ответа в случае успеха.

 - Запрос состояния соединения:

| topic | REQUEST | TYPE1 | TYPE2 | RESPONSE |
|---|---|---|---|---|
| /mpd/connectionState | `{}` | CONNECTION_STATE | CONNECTION_STATE | true |

 - Изменение состояния соединения:

| topic | REQUEST | TYPE1 | TYPE2 | RESPONSE |
|---|---|---|---|---|
| /mpd/connectionState | true |  | CHANGE_CONNECTION_STATE |  |

 - Запрос устройств вывода звука:
 
 | topic | REQUEST | TYPE1 | TYPE2 | RESPONSE |
 |---|---|---|---|---|
 | /mpd/connectionState | `{}` | OUTPUT | OUTPUT | `{ [ {id: 1, name: 'my output', enabled: true }, {id: 2, name: 'my output2', enabled: true } ] }` |

 - Включение/отключение устройства вывода звука:
 
 | topic | REQUEST | TYPE1 | TYPE2 | RESPONSE |
 |---|---|---|---|---|
 | /mpd/output/change | `{id: 1, name: 'my output', enabled: true }` |  | OUTPUT |  |

 - Плейер. Начать воспроизведение трека по id / позиции в плейлисте / позиции в плейлисте с перемоткой на время

 | topic | REQUEST | TYPE1 | TYPE2 | RESPONSE |
 |---|---|---|---|---|
 | /mpd/player/playid | `12` |  | PLAYER |  |
 | /mpd/player/playpos | `14` |  | PLAYER |  |
 | /mpd/player/seek | `{songPos: 12, seekPos: 123}` |  | PLAYER |  |
 
 - Плейер. Управление.
 
 | topic | REQUEST | TYPE1 | TYPE2 | RESPONSE |
 |---|---|---|---|---|
 | /mpd/player/playid | `{}` |  | PLAYER |  |
 
 - Получить текущий плейлист
 
  | topic | REQUEST | TYPE1 | TYPE2 | RESPONSE |
  |---|---|---|---|---|
  | /mpd/playlist | `{cmd: 'PLAY'}` | PLAYLIST | PLAYLIST | см под таблицей |
 
  ```js
 {
   "playlistItems": [
     {
       "file": "Андрей Вербицкий - Безжалостный край (Дмитрий Полонецкий)/01_Prolog.mp3",
       "time": 471,
       "artist": "Вербицкий Андрей",
       "title": "Пролог",
       "album": "Хроники Зареченска. Книга 1. Безжалостный край",
       "track": "1",
       "pos": 0,
       "id": 3326
     },
     {
       "file": "Андрей Вербицкий - Безжалостный край (Дмитрий Полонецкий)/02_Glava_01_01.mp3",
       "time": 996,
       "artist": "Вербицкий Андрей",
       "title": "Глава_01_01",
       "album": "Хроники Зареченска. Книга 1. Безжалостный край",
       "track": "2",
       "pos": 1,
       "id": 3327
     },
     ...
      ]
  }
 ```
 
 - Добавить в текущий плейлист папку/файл
 
  | topic | REQUEST | TYPE1 | TYPE2 | RESPONSE |
  |---|---|---|---|---|
  | /mpd/playlist/add | `{path: '/music/rock', pos: 12}` <sup>*</sup> |  | PLAYLIST_ADD |  |
  | /mpd/playlist/addFile | `{path: 'PLAY', pos: 12}` <sup>*</sup> |  | PLAYLIST_ADD_FILE |  |
 <sup>*</sup> - поле pos не является обязательным. При отсутствии вставка производится в конец плейлиста.
 
 - Очистить текущий плейлист
 
  | topic | REQUEST | TYPE1 | TYPE2 | RESPONSE |
  |---|---|---|---|---|
  | /mpd/playlist/clear | `{}` |  | PLAYLIST_CLEAR |  |
 
 - Перемешать текущий плейлист
 
  | topic | REQUEST | TYPE1 | TYPE2 | RESPONSE |
  |---|---|---|---|---|
  | /mpd/playlist/shuffle | `{from: 10, to: 20}` <sup>*</sup> |  | PLAYLIST_SHUFFLE |  |
  <sup>*</sup> from и to не являются обязательными. При отстуствии перемешивается весь плейлист.
 
 - Статус. Получить текущий статус
 
  | topic | REQUEST | TYPE1 | TYPE2 | RESPONSE |
  |---|---|---|---|---|
  | /mpd/status | `{}` | STATUS | STATUS | см под таблицей |
 
 ```js
{
  volume: -1,
  repeat: false,
  random: false,
  single: false,
  consume: false,
  playlist: "4302",
  playlistlength: 57,
  xfade: 0,
  state: "play",
  song: 4,
  songid: 3330,
  bitrate: 160,
  audio: "44100:24:2",
  nextsong: 5,
  nextsongid: 3331
}
```
 
 - Изменить параметры воспроизведения random|single|repeat|consume 

  
 | topic | REQUEST | TYPE1 | TYPE2 | RESPONSE |
 |---|---|---|---|---|
 | /mpd/status/random | `true` |  | RANDOM |  |
 | /mpd/status/single | `true` |  | SINGLE |  |
 | /mpd/status/repeat | `true` |  | REPEAT |  |
 | /mpd/status/consume | `true` |  | CONSUME |  |
 
 - Получить список сохраненных плейлистов
 
  | topic | REQUEST | TYPE1 | TYPE2 | RESPONSE |
  |---|---|---|---|---|
  | /mpd/storedPlaylist | `{}` | STORED_PLAYLISTS | STORED_PLAYLISTS | см под таблицей |
 
 ```js
 [
   {
     "playlistItems": [
       {
         "file": "Андрей Вербицкий - Безжалостный край (Дмитрий Полонецкий)/01_Prolog.mp3",
         "time": 471,
         "artist": "Вербицкий Андрей",
         "title": "Пролог",
         "album": "Хроники Зареченска. Книга 1. Безжалостный край",
         "track": "1",
         "pos": 0,
         "id": 0
       },
       ...
       {
         "file": "Андрей Вербицкий - Безжалостный край (Дмитрий Полонецкий)/57_Epilog.mp3",
         "time": 470,
         "artist": "Вербицкий Андрей",
         "title": "Эпилог",
         "album": "Хроники Зареченска. Книга 1. Безжалостный край",
         "track": "57",
         "pos": 0,
         "id": 0
       }
     ],
     "name": "Хроники Зареченска 1",
     "lastModified": "2019-09-12 04:38:07"
   },
   ...
 ]
 ```
 
 - Получить детальную информацию о сохраненном плейлисте

  | topic | REQUEST | TYPE1 | TYPE2 | RESPONSE |
  |---|---|---|---|---|
  | /mpd/storedPlaylist/info | `{name: 'rock'}` | STORED_PLAYLIST | STORED_PLAYLIST | см под таблицей |
 
   ```js
  {
    "playlistItems": [
      {
        "file": "Андрей Вербицкий - Безжалостный край (Дмитрий Полонецкий)/01_Prolog.mp3",
        "time": 471,
        "artist": "Вербицкий Андрей",
        "title": "Пролог",
        "album": "Хроники Зареченска. Книга 1. Безжалостный край",
        "track": "1",
        "pos": 0,
        "id": 3326
      },
      {
        "file": "Андрей Вербицкий - Безжалостный край (Дмитрий Полонецкий)/02_Glava_01_01.mp3",
        "time": 996,
        "artist": "Вербицкий Андрей",
        "title": "Глава_01_01",
        "album": "Хроники Зареченска. Книга 1. Безжалостный край",
        "track": "2",
        "pos": 1,
        "id": 3327
      },
      ...
      ]
  }
  ```
 - Установить сохранный плейлист в качестве текущего / добавить в текущий плейлист
 
 | topic | REQUEST | TYPE1 | TYPE2 | RESPONSE |
 |---|---|---|---|---|
 | /mpd/storedPlaylist/load | `{storedPlaylist : 'some playlist'}` |  | STORED_PLAYLIST_LOAD |  |
 | /mpd/storedPlaylist/add | `{storedPlaylist : 'some playlist', pos: 12}`  | <sup>*</sup> | STORED_PLAYLIST_ADD |  |
 
 <sup>*</sup> - поле pos не является обязательным. При отсутствии вставка производится в конец плейлиста.
  
 - Удалить сохраненный плейлист / сохранить текущий / переименовать сохраненный
 
  | topic | REQUEST | TYPE1 | TYPE2 | RESPONSE |
  |---|---|---|---|---|
  | /mpd/storedPlaylist/rm | `{name : 'some playlist'}` |  | STORED_PLAYLIST_RM |  |
  | /mpd/storedPlaylist/save | `{name : 'some playlist'}` |  | STORED_PLAYLIST_SAVE |  |
  | /mpd/storedPlaylist/rename | `{oldName : 'some old name playlist', newName: 'some new name playlist' }` |  | STORED_PLAYLIST_RENAME |  |
  
 
 - Стриминг аудио. Получить url-стриминга
 
 | topic | REQUEST | TYPE1 | TYPE2 | RESPONSE |
 |---|---|---|---|---|
 | /mpd/streamPlayer | `{}` | STREAM_URL |  | 'http://localhost:8000/mpd.mp3' |

 - Получить дерево музыкальной коллекции
 
   | topic | REQUEST | TYPE1 | TYPE2 | RESPONSE |
   |---|---|---|---|---|
   | /mpd/tree | `{}` | TREE | TREE | см под таблицей |
 
 ```js
{
  "children": [
    {
      "file": "Call@8 800 555-17-(88005551744)_20180503153100.mp3"
    },
    {
      "file": "Наутилус_Помпилиус_-_Дыхание(1).mp3"
    },...
    {
      "directory": "(Audiobook_Rus_2009)_Geiman_Amerikanskie_Bogi_by_Lutz_Records",
      "children": [
        {
          "directory": "part1",
          "children": [
            {
              "file": "01-01-001.mp3"
            },
            ...
            ]
        }
        ]
        ...
     }
     ]
  }
```
 
 
 - Получить дерево музыкальной коллекции (детальная информация)
 
   | topic | REQUEST | TYPE1 | TYPE2 | RESPONSE |
   |---|---|---|---|---|
   | /mpd/fullTree | `{}` | FULL_TREE | FULL_TREE | todo |
   
   *** не рекомендуется к использованию, т.к. возвращает очнь много данных
 
 - Отправить запрос на mpd-сервер на обновление информации о музыкальной коллекции
 
  | topic | REQUEST | TYPE1 | TYPE2 | RESPONSE |
  |---|---|---|---|---|
  | /mpd/updateDb | `{path: '/music/rock/123/'}` |  | UPDATE_DB |  |
 
 Сервис обновления информации о текущем времени воспроизведения.
 Отправляет уведомления по шедулеру (2-а раза в секунду). Отправка осуществляется, только если установлен коннект и воспроизведение в режиме "play"
 Отправляет уведомления в топик `/topic/songTime` Формат сообщения:
 
 ```js
{
    songPos: 12,
    playing: true,
    songTime: {
      current: 200,
      full: 350
    }
}
```


todo: описание ui