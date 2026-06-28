import type { Alternativa } from '../../../types/questao';
import styles from './QuizOption.module.scss';

type QuizOptionState = 'default' | 'selected' | 'correct' | 'wrong';

interface QuizOptionProps {
  letter: Alternativa;
  text: string;
  state?: QuizOptionState;
  onClick?: () => void;
  disabled?: boolean;
}

export function QuizOption({ letter, text, state = 'default', onClick, disabled }: QuizOptionProps) {
  const stateClass =
    state !== 'default' ? styles[`option--${state}-${letter}`] : '';

  return (
    <button
      className={`${styles.option} ${stateClass}`}
      onClick={onClick}
      disabled={disabled}
    >
      <span className={styles.letter}>{letter}</span>
      <span className={styles.text}>{text}</span>
    </button>
  );
}
