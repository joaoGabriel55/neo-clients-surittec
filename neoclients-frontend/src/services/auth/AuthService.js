import { API } from '../Api'
const dotenv = require('dotenv');
dotenv.config()

export const userLogin = (userCrendentials) => {
    return API.post('/auth', userCrendentials)
}