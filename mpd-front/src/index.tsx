import * as React from 'react';
import * as ReactDOM from 'react-dom';
import {applyMiddleware, createStore} from 'redux';
import {Provider} from 'react-redux';
import reducers from 'reducers';
import {composeWithDevTools} from "redux-devtools-extension";
import thunk from "redux-thunk";
import {socketMiddleware} from 'redux/SockJSMiddleware'
import Socks from 'components/Socks';

export const store = createStore(reducers, composeWithDevTools(applyMiddleware(socketMiddleware, thunk)));

ReactDOM.render(<Provider store={store}>
    <Socks/>
</Provider>, document.getElementById('root'));