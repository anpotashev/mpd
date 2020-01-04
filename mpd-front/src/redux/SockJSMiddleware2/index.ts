import {Dispatch} from 'redux';
import * as Actions from 'actions';
import {WS_CHECK, WS_REQUEST, WS_SUCCESS, WsDestination, WS_FAILED} from "constants/Socks";

interface IWsRequestPayload {
    type: WsDestination;
    timeout: number | undefined,
    msg: any;
}

interface IWsResponsePayload {
    type: string;
    payload: any;
}


export enum LOADING {
    notLoading,
    loading,
    loaded
}

export const PROCESSING_SUFFIX = '_PROCESSING';
export const FAILED_SUFFIX = '_FAILED';
export const SUCCESS_SUFFIX = '_SUCCESS';

export const socketMiddleware2 = (function () {
    let processing: string[] = []; // массив для хранения сообщений в статус "processing"

    const processSendRequest = (store: any, action: any) => {
        let payload: IWsRequestPayload = action.payload;


        // Если задан timeout, то устанавливаем статус processing и
        // инициируем проверку факта получения ответа через timeout милисекунд
        if (payload.timeout) {
            if (!processing.includes(payload.type.getType())) {
                processing.push(payload.type.getType());
            }
            store.dispatch(function (dispatch: Dispatch) {
                dispatch({type: payload.type.getType() + PROCESSING_SUFFIX});
                setTimeout(() => dispatch({type: WS_CHECK, payload: {type: payload.type.getType()}}), payload.timeout);
            });
        }
        store.dispatch(Actions.sendMessage({type: payload.type, msg: payload.msg}));
    };

    const processCheckRequest = (store: any, action: any) => {
        //Проверяем был-ли получен ответ на запрос. Если нет, то бросаем результат с ошибкой.
        let payload: IWsResponsePayload = action.payload;
        let index = processing.indexOf(payload.type);
        if (index !== -1) {
            processing.splice(index, 1);
            store.dispatch({type: payload.type + FAILED_SUFFIX, payload: 'timeout getting answer'});
        }
    };

    const processSuccessRequest = (store: any, action: any) => {
        let payload: IWsResponsePayload = action.payload;

        let index = processing.indexOf(payload.type);
        if (index !== -1) processing.splice(index, 1);
        store.dispatch({
            type: payload.type + SUCCESS_SUFFIX,
            payload: payload.payload
        });
    };

    const processFailedRequest = (store: any, action: any) => {
        let payload = action.payload;
        let index = processing.indexOf(payload.type);
        if (index !== -1) processing.splice(index, 1);
        store.dispatch({
            type: payload.type + FAILED_SUFFIX,
            payload: payload.msg
        });
    };

    return (store: any) => (next: any) => (action: any) => {
        switch (action.type) {
            case WS_REQUEST:
                //Отправка запроса
                if (action.payload === undefined) {
                    break;
                }
                processSendRequest(store, action);
                break;
            case WS_CHECK:
                //Закончился таймаут, проверяем был-ли получен ответ.
                if (action.payload === undefined) {
                    break;
                }
                processCheckRequest(store, action);
                break;
            case WS_SUCCESS:
                //получен ответ без ошибки
                if (action.payload === undefined) {
                    break;
                }
                processSuccessRequest(store, action);
                break;
            case WS_FAILED:
                //получен ответ с ошибкой
                if (action.payload === undefined) {
                    break;
                }
                processFailedRequest(store, action);
                break;
            default:
                return next(action);
        }
    }

})();
