import api from './api';
import type {
  PartidaCreateRequest,
  EntrarPartidaRequest,
  PartidaResponse,
  ParticipacaoResponse,
  LobbyPartidaResponse,
  PartidaQuestaoResponse,
  RespostaPartidaRequest,
  RespostaPartidaResponse,
  HistoricoPartidaResponse,
} from '../types/partida';

const partidaService = {
  async create(data: PartidaCreateRequest): Promise<PartidaResponse> {
    const res = await api.post<PartidaResponse>('/partidas', data);
    return res.data;
  },

  async join(data: EntrarPartidaRequest): Promise<ParticipacaoResponse> {
    const res = await api.post<ParticipacaoResponse>('/partidas/entrar', data);
    return res.data;
  },

  async get(id: number): Promise<PartidaResponse> {
    const res = await api.get<PartidaResponse>(`/partidas/${id}`);
    return res.data;
  },

  async getParticipants(id: number): Promise<LobbyPartidaResponse> {
    const res = await api.get<LobbyPartidaResponse>(`/partidas/${id}/participantes`);
    return res.data;
  },

  async start(id: number): Promise<PartidaResponse> {
    const res = await api.post<PartidaResponse>(`/partidas/${id}/iniciar`);
    return res.data;
  },

  async cancel(id: number): Promise<PartidaResponse> {
    const res = await api.post<PartidaResponse>(`/partidas/${id}/cancelar`);
    return res.data;
  },

  async finalize(id: number): Promise<PartidaResponse> {
    const res = await api.post<PartidaResponse>(`/partidas/${id}/finalizar`);
    return res.data;
  },

  async getQuestions(id: number): Promise<PartidaQuestaoResponse[]> {
    const res = await api.get<PartidaQuestaoResponse[]>(`/partidas/${id}/questoes`);
    return res.data;
  },

  async submitAnswer(id: number, data: RespostaPartidaRequest): Promise<RespostaPartidaResponse> {
    const res = await api.post<RespostaPartidaResponse>(`/partidas/${id}/respostas`, data);
    return res.data;
  },

  async getLeaderboard(id: number): Promise<ParticipacaoResponse[]> {
    const res = await api.get<ParticipacaoResponse[]>(`/partidas/${id}/leaderboard`);
    return res.data;
  },

  async getHistory(): Promise<HistoricoPartidaResponse[]> {
    const res = await api.get<HistoricoPartidaResponse[]>('/partidas/historico');
    return res.data;
  },
};

export default partidaService;
