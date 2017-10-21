import React from 'react';
import { Provider } from 'react-redux';
import configureStore from './configureStore';
import Routes from './routes';
import baseHistory from './history';
import Header from './Header';
import Footer from './Footer';

const { store } = configureStore(baseHistory, window.__INITIAL_STATE__);

export default () => {
  return (
    <Provider store={store}>
      <div>
        <Header />
        <Routes />
      </div>
    </Provider>
  );
};
