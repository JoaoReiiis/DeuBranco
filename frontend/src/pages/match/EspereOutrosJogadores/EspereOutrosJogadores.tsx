import { useCallback } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useAuth } from '../../../hooks/useAuth';
import { useMatch } from '../../../hooks/useMatch';
import { useMatchSocket } from '../../../hooks/useMatchSocket';
import type { PartidaEventoResponse, ParticipacaoResponse } from '../../../types/partida';
import { LeaderboardRow } from '../../../components/domain/LeaderboardRow/LeaderboardRow';
import styles from './EspereOutrosJogadores.module.scss';

export function EspereOutrosJogadores() {
  const { matchId: paramId } = useParams<{ matchId: string }>();
  const navigate = useNavigate();
  const { token } = useAuth();
  const match = useMatch();
  const matchId = Number(paramId);

  const handleStatus = useCallback((e: PartidaEventoResponse) => {
    if (e.tipo === 'PARTIDA_FINALIZADA') {
      const dados = e.dados as ParticipacaoResponse[];
      if (Array.isArray(dados)) match.setLeaderboard(dados);
      navigate(`/match/${matchId}/podio`, { replace: true });
    }
  }, [navigate, matchId, match]);

  const handleLeaderboard = useCallback((e: PartidaEventoResponse) => {
    if (e.tipo === 'LEADERBOARD_ATUALIZADO') {
      const dados = e.dados as ParticipacaoResponse[];
      if (Array.isArray(dados)) match.setLeaderboard(dados);
    }
  }, [match]);

  const handlePlacar = useCallback((e: PartidaEventoResponse) => {
    if (e.tipo === 'PLACAR_ATUALIZADO') {
      const dados = e.dados as ParticipacaoResponse[];
      if (Array.isArray(dados)) match.setLeaderboard(dados);
    }
  }, [match]);

  useMatchSocket({
    matchId,
    token: token ?? '',
    onLobby: () => {},
    onStatus: handleStatus,
    onLeaderboard: handleLeaderboard,
    onPlacar: handlePlacar,
    onResposta: () => {},
  });

  const sorted = [...match.leaderboard].sort((a, b) => b.scorePartida - a.scorePartida);

  return (
    <div className={styles.page}>
      <span className={styles.icon}>⏳</span>
      <div>
        <h2 className={styles.title}>Aguardando os outros jogadores...</h2>
        <p className={styles.subtitle}>Você já respondeu todas as questões</p>
      </div>
      {sorted.length > 0 && (
        <div className={styles.placarSection}>
          <span className={styles.placarTitle}>Placar parcial</span>
          {sorted.map((p, i) => (
            <LeaderboardRow key={p.id} rank={i + 1} nome={p.jogador.nome} score={p.scorePartida} />
          ))}
        </div>
      )}
    </div>
  );
}
