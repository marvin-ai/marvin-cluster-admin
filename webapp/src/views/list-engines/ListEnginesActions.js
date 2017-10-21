import axios from 'axios';
import configApi from '../../config/configApi';

export const getListEngines = () => {
  const request = axios.get(`${configApi.API_URL}/engines`);
  return {
    type: 'LIST_ENGINES_FETCHED',
    payload: request,
  };
};