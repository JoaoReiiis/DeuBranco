import styles from './ProgressBar.module.scss';

interface ProgressBarProps {
  value: number;
  max: number;
}

export function ProgressBar({ value, max }: ProgressBarProps) {
  const percent = Math.max(0, Math.min(100, (value / max) * 100));
  const variant = percent <= 20 ? 'danger' : percent <= 40 ? 'warning' : '';

  return (
    <div className={styles.track}>
      <div
        className={[styles.fill, variant ? styles[`fill--${variant}`] : ''].filter(Boolean).join(' ')}
        style={{ width: `${percent}%` }}
      />
    </div>
  );
}
