import { createContext, useState, useCallback, type ReactNode } from 'react';
import type { PartidaQuestaoResponse, ParticipacaoResponse, RespostaPartidaResponse, PartidaStatus } from '../types/partida';

interface MatchContextValue {
  matchId: number | null;
  status: PartidaStatus | null;
  questions: PartidaQuestaoResponse[];
  participants: ParticipacaoResponse[];
  answers: RespostaPartidaResponse[];
  leaderboard: ParticipacaoResponse[];
  tempoDeJogo: number;
  isHost: boolean;
  hostId: number | null;
  codigoPin: string;
  setMatchId: (id: number) => void;
  setStatus: (s: PartidaStatus) => void;
  setQuestions: (q: PartidaQuestaoResponse[]) => void;
  setParticipants: (p: ParticipacaoResponse[]) => void;
  addAnswer: (r: RespostaPartidaResponse) => void;
  setLeaderboard: (l: ParticipacaoResponse[]) => void;
  setTempoDeJogo: (t: number) => void;
  setIsHost: (v: boolean) => void;
  setHostId: (id: number) => void;
  setCodigoPin: (pin: string) => void;
  reset: () => void;
}

export const MatchContext = createContext<MatchContextValue | null>(null);

export function MatchProvider({ children }: { children: ReactNode }) {
  const [matchId, setMatchId] = useState<number | null>(null);
  const [status, setStatus] = useState<PartidaStatus | null>(null);
  const [questions, setQuestions] = useState<PartidaQuestaoResponse[]>([]);
  const [participants, setParticipants] = useState<ParticipacaoResponse[]>([]);
  const [answers, setAnswers] = useState<RespostaPartidaResponse[]>([]);
  const [leaderboard, setLeaderboard] = useState<ParticipacaoResponse[]>([]);
  const [tempoDeJogo, setTempoDeJogo] = useState(30);
  const [isHost, setIsHost] = useState(false);
  const [hostId, setHostId] = useState<number | null>(null);
  const [codigoPin, setCodigoPin] = useState('');

  const addAnswer = useCallback((r: RespostaPartidaResponse) => {
    setAnswers(prev => [...prev, r]);
  }, []);

  const reset = useCallback(() => {
    setMatchId(null);
    setStatus(null);
    setQuestions([]);
    setParticipants([]);
    setAnswers([]);
    setLeaderboard([]);
    setTempoDeJogo(30);
    setIsHost(false);
    setHostId(null);
    setCodigoPin('');
  }, []);

  return (
    <MatchContext.Provider value={{
      matchId, status, questions, participants, answers, leaderboard,
      tempoDeJogo, isHost, hostId, codigoPin,
      setMatchId, setStatus, setQuestions, setParticipants, addAnswer,
      setLeaderboard, setTempoDeJogo, setIsHost, setHostId, setCodigoPin, reset,
    }}>
      {children}
    </MatchContext.Provider>
  );
}
