import api from './api';
import type { LoginRequest, LoginResponse, JogadorResponse } from '../types/auth';

const authService = {
  async login(data: LoginRequest): Promise<LoginResponse> {
    const res = await api.post<LoginResponse>('/auth/login', data);
    return res.data;
  },

  async me(): Promise<JogadorResponse> {
    const res = await api.get<JogadorResponse>('/jogadores/me');
    return res.data;
  },
};

export default authService;
