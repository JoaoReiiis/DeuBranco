import type { PartidaQuestaoResponse } from '../../../types/partida';
import styles from './QuestionCard.module.scss';

interface QuestionCardProps {
  question: PartidaQuestaoResponse;
  current: number;
  total: number;
}

export function QuestionCard({ question, current, total }: QuestionCardProps) {
  return (
    <div className={styles.card}>
      <div className={styles.meta}>
        <span className={styles.discipline}>{question.disciplina.replace('_', ' ')}</span>
        <span className={styles.counter}>· {current}/{total}</span>
      </div>
      <p className={styles.enunciado}>{question.enunciado}</p>
      {question.imagens && question.imagens.length > 0 && (
        <div className={styles.images}>
          {question.imagens.map((src, i) => (
            <img key={i} src={src} alt={`Imagem ${i + 1} da questão`} />
          ))}
        </div>
      )}
    </div>
  );
}
