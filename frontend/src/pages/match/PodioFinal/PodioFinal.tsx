import { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useMatch } from '../../../hooks/useMatch';
import partidaService from '../../../services/partidaService';
import { LeaderboardRow } from '../../../components/domain/LeaderboardRow/LeaderboardRow';
import { Button } from '../../../components/ui/Button/Button';
import styles from './PodioFinal.module.scss';

export function PodioFinal() {
  const { matchId: paramId } = useParams<{ matchId: string }>();
  const navigate = useNavigate();
  const match = useMatch();
  const matchId = Number(paramId);

  useEffect(() => {
    if (match.leaderboard.length === 0) {
      partidaService.getLeaderboard(matchId).then(match.setLeaderboard);
    }
  }, [matchId, match]);

  const sorted = [...match.leaderboard].sort((a, b) => b.scorePartida - a.scorePartida);

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <div className={styles.trophy}>🏆</div>
        <h1 className={styles.title}>Resultado final</h1>
      </div>

      <div>
        <p className={styles.sectionTitle}>Classificação</p>
        <div className={styles.list}>
          {sorted.map((p, i) => (
            <LeaderboardRow key={p.id} rank={i + 1} nome={p.jogador.nome} score={p.scorePartida} />
          ))}
        </div>
      </div>

      <div className={styles.actions}>
        <Button onClick={() => navigate(`/match/${matchId}/finalizada`)}>
          Ver gabarito
        </Button>
        <Button variant="secondary" onClick={() => { match.reset(); navigate('/'); }}>
          Voltar ao início
        </Button>
      </div>
    </div>
  );
}
