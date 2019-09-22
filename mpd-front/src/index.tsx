import * as React from 'react';
import * as ReactDOM from 'react-dom';
import {applyMiddleware, createStore} from 'redux';
import {Provider} from 'react-redux';
import reducers from 'reducers';
import {socketMiddleware} from 'redux/SockJSMiddleware'
import Socks from 'components/Socks';
import {socksConnect} from 'actions';
import {socketMiddleware2} from "./redux/SockJSMiddleware2";
import {composeWithDevTools} from "redux-devtools-extension";
import thunk from "redux-thunk";
import {ErrorsPanel} from "./components/ErrorsPanel";

export const store = createStore(reducers, composeWithDevTools(applyMiddleware(socketMiddleware, socketMiddleware2, thunk)));

store.dispatch(socksConnect());

ReactDOM.render(<Provider store={store}>
    <ErrorsPanel/>
    <Socks/>
</Provider>, document.getElementById('root'));