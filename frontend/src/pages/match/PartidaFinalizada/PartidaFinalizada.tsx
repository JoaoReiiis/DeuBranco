import { useNavigate, useParams } from 'react-router-dom';
import { useMatch } from '../../../hooks/useMatch';
import type { Alternativa } from '../../../types/questao';
import { QuizOption } from '../../../components/domain/QuizOption/QuizOption';
import { QuestionCard } from '../../../components/domain/QuestionCard/QuestionCard';
import { Button } from '../../../components/ui/Button/Button';
import styles from './PartidaFinalizada.module.scss';

const LETTERS: Alternativa[] = ['A', 'B', 'C', 'D', 'E'];

export function PartidaFinalizada() {
  const { matchId: paramId } = useParams<{ matchId: string }>();
  const navigate = useNavigate();
  const match = useMatch();
  const matchId = Number(paramId);

  const optionMap = (q: typeof match.questions[0]): Record<Alternativa, string> => ({
    A: q.opcaoA, B: q.opcaoB, C: q.opcaoC, D: q.opcaoD, E: q.opcaoE,
  });

  function getState(questionId: number, letter: Alternativa): 'default' | 'selected' | 'correct' | 'wrong' {
    const answer = match.answers.find(a => a.partidaQuestaoId === questionId);
    if (!answer) return 'default';
    if (answer.alternativaMarcada?.toUpperCase() === letter) {
      return answer.acertou ? 'correct' : 'wrong';
    }
    return 'default';
  }

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <button className={styles.back} onClick={() => navigate(`/match/${matchId}/podio`)}>←</button>
        <h1 className={styles.title}>Gabarito</h1>
      </div>

      {match.questions.map((q, i) => (
        <div key={q.id} className={styles.questionBlock}>
          <QuestionCard question={q} current={i + 1} total={match.questions.length} />
          <div className={styles.options}>
            {LETTERS.map((l) => (
              <QuizOption
                key={l}
                letter={l}
                text={optionMap(q)[l]}
                state={getState(q.id, l)}
                disabled
              />
            ))}
          </div>
          {i < match.questions.length - 1 && <div className={styles.divider} />}
        </div>
      ))}

      <Button fullWidth variant="secondary" onClick={() => { match.reset(); navigate('/'); }}>
        Voltar ao início
      </Button>
    </div>
  );
}
