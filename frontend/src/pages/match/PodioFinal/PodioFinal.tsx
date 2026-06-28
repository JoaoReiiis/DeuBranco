import { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useMatch } from '../../../hooks/useMatch';
import partidaService from '../../../services/partidaService';
import { Button } from '../../../components/ui/Button/Button';
import styles from './PodioFinal.module.scss';

const MEDALS = ['🥇', '🥈', '🥉'];

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
  const top3 = sorted.slice(0, 3);

  const myEntry = sorted[0]; // placeholder — we'd need current user id to find theirs
  const totalAnswers = match.answers.length;
  const correct = match.answers.filter(a => a.acertou).length;
  const accuracy = totalAnswers > 0 ? Math.round((correct / totalAnswers) * 100) : 0;

  return (
    <div className={styles.page}>
      <div className={styles.inner}>
        <h1 className={styles.title}>Resultados da Sessão</h1>

        {/* Podium */}
        {top3.length > 0 && (
          <div className={styles.podium}>
            {top3.map((p, i) => (
              <div key={p.id} className={`${styles.podiumSlot} ${i === 0 ? styles['podiumSlot--first'] : ''}`}>
                <div className={styles.podiumAvatar}>
                  {p.jogador.nome.charAt(0).toUpperCase()}
                </div>
                <div className={styles.podiumMedal}>{MEDALS[i] ?? `#${i + 1}`}</div>
                <span className={styles.podiumName}>{p.jogador.nome.split(' ')[0]}</span>
                <span className={styles.podiumScore}>{p.scorePartida} pts</span>
              </div>
            ))}
          </div>
        )}

        {/* Stats cards */}
        <div className={styles.statsGrid}>
          <div className={styles.statCard}>
            <span className={styles.statLabel}>Precisão</span>
            <span className={`${styles.statValue} ${accuracy >= 70 ? styles['statValue--up'] : styles['statValue--down']}`}>
              {accuracy}%
            </span>
          </div>
          <div className={styles.statCard}>
            <span className={styles.statLabel}>Questões corretas</span>
            <span className={styles.statValue}>{correct}/{totalAnswers}</span>
          </div>
          <div className={styles.statCard}>
            <span className={styles.statLabel}>Pontos</span>
            <span className={styles.statValue}>{myEntry?.scorePartida ?? 0}</span>
          </div>
        </div>

        {/* Full leaderboard */}
        {sorted.length > 3 && (
          <div className={styles.leaderboard}>
            <h2 className={styles.sectionTitle}>Classificação completa</h2>
            {sorted.map((p, i) => (
              <div key={p.id} className={styles.leaderRow}>
                <span className={styles.leaderRank}>#{i + 1}</span>
                <span className={styles.leaderName}>{p.jogador.nome}</span>
                <span className={styles.leaderScore}>{p.scorePartida} pts</span>
              </div>
            ))}
          </div>
        )}

        {/* Actions */}
        <div className={styles.actions}>
          <Button onClick={() => navigate(`/match/${matchId}/finalizada`)}>
            Revisão da Partida
          </Button>
          <Button variant="secondary" onClick={() => { match.reset(); navigate('/'); }}>
            Voltar ao início
          </Button>
        </div>
      </div>
    </div>
  );
}
