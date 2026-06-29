import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';
import { Input } from '../../components/ui/Input/Input';
import { Button } from '../../components/ui/Button/Button';
import partidaService from '../../services/partidaService';
import classroomImg from '../../assets/classroom.png';
import styles from './Home.module.scss';

export function Home() {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [pin, setPin] = useState('');
  const [pinError, setPinError] = useState('');
  const [loading, setLoading] = useState(false);
  const [totalPontos, setTotalPontos] = useState<number | null>(null);
  const [partidasJogadas, setPartidasJogadas] = useState<number | null>(null);

  useEffect(() => {
    partidaService
      .getHistory()
      .then((historico) => {
        const finalizadas = historico.filter(h => h.statusSala === 'FINALIZADA');
        setTotalPontos(finalizadas.reduce((acc, h) => acc + (h.scorePartida ?? 0), 0));
        setPartidasJogadas(finalizadas.length);
      })
      .catch(() => {
        setTotalPontos(0);
        setPartidasJogadas(0);
      });
  }, []);

  async function handleJoinByPin() {
    if (!pin.trim()) return;
    setPinError('');
    setLoading(true);
    try {
      const participacao = await partidaService.join({ codigoPin: pin.trim().toUpperCase() });
      navigate(`/match/${participacao.partidaId}/lobby`);
    } catch {
      setPinError('PIN inválido ou partida não encontrada.');
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className={styles.page}>
      <div className={styles.inner}>
        <div className={styles.cards}>
          {/* Join Arena */}
          <div className={styles.card}>
            <h2 className={styles.cardTitle}>Join Arena</h2>
            <p className={styles.cardDesc}>
              Digite o código PIN da sala para entrar em uma partida ao vivo.
            </p>
            <div className={styles.pinRow}>
              <Input
                label=""
                value={pin}
                onChange={(e) => setPin(e.target.value.toUpperCase())}
                placeholder="Ex: ABC123"
                error={pinError}
                maxLength={10}
                onKeyDown={(e) => e.key === 'Enter' && handleJoinByPin()}
              />
              <Button onClick={handleJoinByPin} disabled={loading}>
                {loading ? '...' : 'Join →'}
              </Button>
            </div>
          </div>

          {/* Criar sala */}
          <div className={`${styles.card} ${styles['card--create']}`} onClick={() => navigate('/match/configurar')}>
            <div className={styles.createImgWrap}>
              <img src={classroomImg} alt="" className={styles.createImg} draggable={false} />
              <div className={styles.createImgFade} />
            </div>
            <div className={styles.createBody}>
              <h2 className={styles.createTitle}>Criar uma sala</h2>
              <p className={styles.createDesc}>Inicie uma nova sala para convidar seus amigos</p>
              <div className={styles.createBtn}>
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.2" strokeLinecap="round">
                  <circle cx="12" cy="12" r="9" />
                  <line x1="12" y1="8" x2="12" y2="16" />
                  <line x1="8" y1="12" x2="16" y2="12" />
                </svg>
                Host Session
              </div>
            </div>
          </div>
        </div>

        {/* Stats */}
        <div className={styles.stats}>
          <div className={styles.statCard}>
            <span className={styles.statLabel}>Pontuação total</span>
            <span className={styles.statValue}>
              {totalPontos === null ? '—' : totalPontos.toLocaleString('pt-BR')}
            </span>
          </div>
          <div className={styles.statCard}>
            <span className={styles.statLabel}>Partidas jogadas</span>
            <span className={styles.statValue}>
              {partidasJogadas === null ? '—' : partidasJogadas}
            </span>
          </div>
          <div className={styles.statCard}>
            <span className={styles.statLabel}>Média / partida</span>
            <span className={styles.statValue}>
              {totalPontos === null || partidasJogadas === null
                ? '—'
                : partidasJogadas > 0
                  ? Math.round(totalPontos / partidasJogadas).toLocaleString('pt-BR')
                  : 0}
            </span>
          </div>
        </div>

        <p className={styles.greeting}>
          Bem-vindo, <strong>{user?.nome?.split(' ')[0] ?? 'Jogador'}</strong>!
        </p>
      </div>
    </div>
  );
}
