import axios from 'axios';

const TOKEN_KEY = 'db_token';

let logoutCallback: (() => void) | null = null;

export function setLogoutCallback(fn: () => void) {
  logoutCallback = fn;
}

const api = axios.create({
  baseURL: 'http://localhost:8080',
  headers: { 'Content-Type': 'application/json' },
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem(TOKEN_KEY);
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      logoutCallback?.();
    }
    return Promise.reject(error);
  },
);

export { TOKEN_KEY };
export default api;
