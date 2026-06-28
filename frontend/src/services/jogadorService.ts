import api from './api';
import type { JogadorCreateRequest, JogadorUpdateRequest, JogadorResponse } from '../types/auth';

const jogadorService = {
  async register(data: JogadorCreateRequest): Promise<JogadorResponse> {
    const res = await api.post<JogadorResponse>('/jogadores', data);
    return res.data;
  },

  async updateMe(data: JogadorUpdateRequest): Promise<JogadorResponse> {
    const res = await api.put<JogadorResponse>('/jogadores/me', data);
    return res.data;
  },

  async deleteMe(): Promise<void> {
    await api.delete('/jogadores/me');
  },
};

export default jogadorService;
