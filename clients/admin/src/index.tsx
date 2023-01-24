import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import axios from "axios";
import {configureStore} from "./redux/configureStore";
import {Provider} from "react-redux";

axios.defaults.baseURL = 'http://localhost:8000/api/admin/';
axios.defaults.withCredentials = true;

const store = configureStore();

ReactDOM.render(
    <React.StrictMode>
        <Provider store={store}>
            <App/>
        </Provider>
    </React.StrictMode>,
    document.getElementById('root')
);
