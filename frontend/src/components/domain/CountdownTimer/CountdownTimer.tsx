import { ProgressBar } from '../../ui/ProgressBar/ProgressBar';
import styles from './CountdownTimer.module.scss';

interface CountdownTimerProps {
  remaining: number;
  total: number;
}

export function CountdownTimer({ remaining, total }: CountdownTimerProps) {
  const percent = (remaining / total) * 100;
  const timeClass =
    percent <= 20 ? styles['time--danger'] : percent <= 40 ? styles['time--warning'] : '';

  const mins = Math.floor(remaining / 60);
  const secs = remaining % 60;
  const display = mins > 0
    ? `${mins}:${String(secs).padStart(2, '0')}`
    : `${secs}s`;

  return (
    <div className={styles.wrapper}>
      <div className={styles.row}>
        <span className={styles.label}>Tempo</span>
        <span className={`${styles.time} ${timeClass}`}>{display}</span>
      </div>
      <ProgressBar value={remaining} max={total} />
    </div>
  );
}
