import type { JogadorResponse } from './auth';
import type { Alternativa, Disciplina } from './questao';

export type PartidaStatus = 'AGUARDANDO' | 'ANDAMENTO' | 'FINALIZADA' | 'CANCELADA';

export interface PartidaCreateRequest {
  numeroQuestoes: number;
  disciplinas: Disciplina[];
  tempoDeJogo: number;
}

export interface EntrarPartidaRequest {
  codigoPin: string;
}

export interface PartidaResponse {
  id: number;
  host: JogadorResponse;
  tempoDeJogo: number;
  codigoPin: string;
  statusSala: PartidaStatus;
  criadoEm: string;
  iniciadaEm: string | null;
  finalizadaEm: string | null;
}

export interface ParticipacaoResponse {
  id: number;
  partidaId: number;
  jogador: JogadorResponse;
  scorePartida: number;
}

export interface LobbyPartidaResponse {
  partida: PartidaResponse;
  participantes: ParticipacaoResponse[];
}

export interface PartidaQuestaoResponse {
  id: number;
  ordem: number;
  enunciado: string;
  opcaoA: string;
  opcaoB: string;
  opcaoC: string;
  opcaoD: string;
  opcaoE: string;
  disciplina: Disciplina;
  imagens: string[] | null;
}

export interface RespostaPartidaRequest {
  partidaQuestaoId: number;
  alternativaMarcada: Alternativa | null;
  tempoResposta: number;
}

export interface RespostaPartidaResponse {
  id: number;
  participacaoId: number;
  partidaQuestaoId: number;
  alternativaMarcada: Alternativa | null;
  tempoResposta: number;
  acertou: boolean;
  scorePartida: number;
}

export interface HistoricoPartidaResponse {
  partidaId: number;
  codigoPin: string;
  statusSala: PartidaStatus;
  scorePartida: number;
  criadoEm: string;
}

export interface PartidaEventoResponse {
  tipo: string;
  partidaId: number;
  dados: unknown;
  publicadoEm: string;
}
