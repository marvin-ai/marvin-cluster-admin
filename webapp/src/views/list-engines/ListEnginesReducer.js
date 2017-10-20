const INITIAL_STATE = {
    listEngines: [],
  };
  export default (state = INITIAL_STATE, action) => {
    switch (action.type) {
      case 'LIST_ENGINES_FETCHED_FULFILLED':
        return { ...state, listEngines: action.payload };
      default:
        return state;
    }
  };
  