import { useState, useEffect, useCallback, useRef } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useAuth } from '../../../hooks/useAuth';
import { useMatch } from '../../../hooks/useMatch';
import { useMatchSocket } from '../../../hooks/useMatchSocket';
import partidaService from '../../../services/partidaService';
import type { PartidaEventoResponse, ParticipacaoResponse } from '../../../types/partida';
import type { Alternativa } from '../../../types/questao';
import { Logo } from '../../../components/ui/Logo/Logo';
import { QuizOption } from '../../../components/domain/QuizOption/QuizOption';
import { ProgressBar } from '../../../components/ui/ProgressBar/ProgressBar';
import styles from './JogoAtivo.module.scss';

const LETTERS: Alternativa[] = ['A', 'B', 'C', 'D'];

export function JogoAtivo() {
  const { matchId: paramId } = useParams<{ matchId: string }>();
  const navigate = useNavigate();
  const { token } = useAuth();
  const match = useMatch();
  const matchId = Number(paramId);

  const [currentIndex, setCurrentIndex] = useState(0);
  const [selectedAnswer, setSelectedAnswer] = useState<Alternativa | null>(null);
  const [answered, setAnswered] = useState(false);
  const [remaining, setRemaining] = useState(match.tempoDeJogo);
  const startTimeRef = useRef(Date.now());
  const timerRef = useRef<ReturnType<typeof setInterval> | null>(null);

  useEffect(() => {
    if (match.questions.length === 0) {
      partidaService.getQuestions(matchId).then(match.setQuestions);
    }
  }, [matchId, match]);

  useEffect(() => {
    setRemaining(match.tempoDeJogo);
    setAnswered(false);
    setSelectedAnswer(null);
    startTimeRef.current = Date.now();

    timerRef.current = setInterval(() => {
      setRemaining(prev => {
        if (prev <= 1) {
          clearInterval(timerRef.current!);
          handleAnswer(null);
          return 0;
        }
        return prev - 1;
      });
    }, 1000);

    return () => { if (timerRef.current) clearInterval(timerRef.current); };
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [currentIndex, match.tempoDeJogo]);

  async function handleAnswer(alternativa: Alternativa | null) {
    if (answered) return;
    setAnswered(true);
    setSelectedAnswer(alternativa);
    if (timerRef.current) clearInterval(timerRef.current);

    const question = match.questions[currentIndex];
    const tempoResposta = Math.round((Date.now() - startTimeRef.current) / 1000);

    try {
      const res = await partidaService.submitAnswer(matchId, {
        partidaQuestaoId: question.id,
        alternativaMarcada: alternativa,
        tempoResposta,
      });
      match.addAnswer(res);
    } catch {
      // silently fail
    }

    setTimeout(() => {
      if (currentIndex < match.questions.length - 1) {
        setCurrentIndex(prev => prev + 1);
      } else {
        navigate(`/match/${matchId}/espere`, { replace: true });
      }
    }, 800);
  }

  const handleStatus = useCallback((e: PartidaEventoResponse) => {
    if (e.tipo === 'PARTIDA_FINALIZADA') {
      const dados = e.dados as { participantes?: ParticipacaoResponse[] };
      if (dados.participantes) match.setLeaderboard(dados.participantes);
      navigate(`/match/${matchId}/podio`, { replace: true });
    }
  }, [navigate, matchId, match]);

  const handleLeaderboard = useCallback((e: PartidaEventoResponse) => {
    if (e.tipo === 'LEADERBOARD_ATUALIZADO') {
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
    onPlacar: () => {},
    onResposta: () => {},
  });

  const question = match.questions[currentIndex];
  if (!question) return <p className={styles.loading}>Carregando questões...</p>;

  const optionMap: Record<Alternativa, string> = {
    A: question.opcaoA,
    B: question.opcaoB,
    C: question.opcaoC,
    D: question.opcaoD,
    E: question.opcaoE,
  };

  const totalQuestions = match.questions.length;
  const progress = ((currentIndex + 1) / totalQuestions) * 100;

  return (
    <div className={styles.page}>
      {/* Game top bar */}
      <header className={styles.header}>
        <button className={styles.closeBtn} onClick={() => navigate('/')}>✕</button>
        <Logo variant="horizontal" height={28} />
        <span className={styles.pts}>
          {match.leaderboard.length > 0
            ? `${match.leaderboard.find(p => p.jogador)?.scorePartida ?? 0} pts`
            : '0 pts'}
        </span>
      </header>

      <div className={styles.body}>
        {/* Progress */}
        <div className={styles.progress}>
          <span className={styles.questionLabel}>
            Question {currentIndex + 1}/{totalQuestions}
          </span>
          <ProgressBar value={progress} max={100} />
          <div className={styles.timer}>
            <span className={`${styles.timerNum} ${remaining <= 5 ? styles['timerNum--urgent'] : ''}`}>
              {remaining}
            </span>
          </div>
        </div>

        {/* Question */}
        <p className={styles.questionText}>{question.enunciado}</p>

        {/* Options — 2×2 grid */}
        {answered ? (
          <p className={styles.answeredMsg}>Resposta registrada ✓</p>
        ) : (
          <div className={styles.options}>
            {LETTERS.map((l) => (
              <QuizOption
                key={l}
                letter={l}
                text={optionMap[l]}
                state={selectedAnswer === l ? 'selected' : 'default'}
                onClick={() => handleAnswer(l)}
                disabled={answered}
              />
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
