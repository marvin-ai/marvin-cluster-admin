import React from 'react';
import {
    HashRouter,
    Route,
    Redirect,
    Switch
  } from 'react-router-dom'
import './middlewares/axiosHeaders';
import listEngines from './views/list-engines/ListEngines';
import editEngine from './views/edit-engine/EditEngine';

export default props => (

  <HashRouter>
    <div className="container">
      <Switch>
        <Redirect exact from="/" to="/list-engines" />
        <Route path="/list-engines" component={listEngines} />
        <Route path="/edit-engine" component={editEngine} />
      </Switch>
    </div>
  </HashRouter>
);
