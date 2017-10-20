import axios from 'axios';
//import localstorage from '../config/localstorageConfig';

//let token = localStorage.getItem(localstorage.USER_TOKEN) || null
//axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';
