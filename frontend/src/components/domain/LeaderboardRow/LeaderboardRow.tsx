import styles from './LeaderboardRow.module.scss';

interface LeaderboardRowProps {
  rank: number;
  nome: string;
  score: number;
}

const MEDALS: Record<number, string> = { 1: '🥇', 2: '🥈', 3: '🥉' };
const TOP_CLASSES: Record<number, string> = {
  1: styles['row--top1'],
  2: styles['row--top2'],
  3: styles['row--top3'],
};

export function LeaderboardRow({ rank, nome, score }: LeaderboardRowProps) {
  return (
    <div className={`${styles.row} ${TOP_CLASSES[rank] ?? ''}`}>
      {MEDALS[rank] ? (
        <span className={styles.medal}>{MEDALS[rank]}</span>
      ) : (
        <span className={styles.rank}>#{rank}</span>
      )}
      <span className={styles.name}>{nome}</span>
      <span className={styles.score}>{score} pts</span>
    </div>
  );
}
