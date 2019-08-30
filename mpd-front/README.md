***middleware (src/redux/SockJSMiddleware/index.ts***:

* Запрос:
```
{
    type: <TYPE>_WS_REQUEST,
    payload: {
        destination: <DESTINATION>,
        timeout: <TIMEOUT>,
        msg: MSG
    }
}
```
* Если задан timeout, то после запроса будет создан action:
```
{
    type: <TYPE>_WS_PROCESSING
}
```
* Если был задан TIMEOUT и в течение TIMEOUT не получен ответ, то 
```
{
    type: <TYPE>_WS_FAILED
}
```

* Если получен ответ в очереди /user/queue/reply c type=<TYPE>, то:
```
{
    type: <TYPE>_WS_SUCCESS
}
```

* Если получен ответ в очереди /user/queue/error c type=<TYPE>, то:
```
{
    type: <TYPE>_WS_FAILED
}
```
