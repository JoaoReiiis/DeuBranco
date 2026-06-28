import api from './api';
import type { QuestaoResponse, QuestaoRequest, QuestaoFiltros } from '../types/questao';
import type { Page } from '../types/pagination';

const questaoService = {
  async list(params: QuestaoFiltros = {}): Promise<Page<QuestaoResponse>> {
    const res = await api.get<Page<QuestaoResponse>>('/questoes', { params });
    return res.data;
  },

  async getById(id: number): Promise<QuestaoResponse> {
    const res = await api.get<QuestaoResponse>(`/questoes/${id}`);
    return res.data;
  },

  async create(data: QuestaoRequest): Promise<QuestaoResponse> {
    const res = await api.post<QuestaoResponse>('/questoes', data);
    return res.data;
  },

  async update(id: number, data: QuestaoRequest): Promise<QuestaoResponse> {
    const res = await api.put<QuestaoResponse>(`/questoes/${id}`, data);
    return res.data;
  },

  async remove(id: number): Promise<void> {
    await api.delete(`/questoes/${id}`);
  },
};

export default questaoService;
