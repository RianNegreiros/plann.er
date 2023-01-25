import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import axios from 'axios';
import { configureStore } from './redux/confiureStore';
import { Provider } from 'react-redux';

axios.defaults.baseURL = `${process.env.REACT_APP_BASE_URL}/api/ambassador`
axios.defaults.withCredentials = true;

const store = configureStore()

ReactDOM.render(
    <React.StrictMode>
        <Provider store={store}>
            <App/>
        </Provider>
    </React.StrictMode>,
    document.getElementById('root')
);