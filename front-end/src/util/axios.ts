import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api/v1', 
  timeout: 5000, 
  headers: {
    'Content-Type': 'application/json',
  },
});


api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response) {
      console.error('Erro na resposta:', error.response.status, error.response.data);
    } else {
      console.error('Erro na requisição:', error.message);
    }
    return Promise.reject(error);
  }
);

export default api;