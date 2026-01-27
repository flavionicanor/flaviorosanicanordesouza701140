import axios from "axios";

export const api = axios.create({
  baseURL: "http://localhost:8080/api/v1",
  headers: {
    "Content-Type": "application/json",
  },
});

// Interceptor de request (envia token)
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("accessToken");

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

// Interceptor de request (envia token)
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status == 401) {
      //token invalido ou expirado
      localStorage.removeItem("accessToken");

      window.location.href = "/login";
    }

    return Promise.reject(error);
  },
);
