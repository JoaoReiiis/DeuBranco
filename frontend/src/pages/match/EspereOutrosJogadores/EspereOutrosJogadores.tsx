import { useCallback } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useAuth } from '../../../hooks/useAuth';
import { useMatch } from '../../../hooks/useMatch';
import { useMatchSocket } from '../../../hooks/useMatchSocket';
import type { PartidaEventoResponse, ParticipacaoResponse } from '../../../types/partida';
import { Logo } from '../../../components/ui/Logo/Logo';
import { Button } from '../../../components/ui/Button/Button';
import styles from './EspereOutrosJogadores.module.scss';

export function EspereOutrosJogadores() {
  const { matchId: paramId } = useParams<{ matchId: string }>();
  const navigate = useNavigate();
  const { token } = useAuth();
  const match = useMatch();
  const matchId = Number(paramId);

  const myScore = match.answers.reduce((acc, a) => acc + (a.acertou ? 100 : 0), 0);

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

  return (
    <div className={styles.page}>
      {/* Game mini-header */}
      <header className={styles.header}>
        <button className={styles.closeBtn} onClick={() => navigate('/')}>✕</button>
        <Logo variant="horizontal" height={28} />
        <span className={styles.pts}>{myScore} pts</span>
      </header>

      {/* Centered content */}
      <div className={styles.body}>
        <span className={styles.emoji}>🎉</span>
        <h1 className={styles.title}>Muito Bem!</h1>
        <p className={styles.desc}>Aguarde a finalização da partida para ver o resultado final!</p>
        <p className={styles.desc}>Caso queira revisar ou alterar alguma alternativa, clique no botão abaixo.</p>
        <Button
          variant="secondary"
          onClick={() => navigate(`/match/${matchId}/finalizada`)}
        >
          Revisar respostas
        </Button>
      </div>
    </div>
  );
}
