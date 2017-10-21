import { combineReducers } from 'redux';
import { routerReducer } from 'react-router-redux';
import listEnginesReducer from './views/list-engines/ListEnginesReducer';

const rootReducer = combineReducers({
    routing: routerReducer,
    engines: listEnginesReducer,
});

export default rootReducer;
