import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';
import { Input } from '../../components/ui/Input/Input';
import { Button } from '../../components/ui/Button/Button';
import partidaService from '../../services/partidaService';
import styles from './Home.module.scss';

export function Home() {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [pin, setPin] = useState('');
  const [pinError, setPinError] = useState('');
  const [loading, setLoading] = useState(false);

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
            <h2 className={styles.cardTitle}>Criar Arena</h2>
            <p className={styles.cardDesc}>
              Configure e inicie uma nova partida para desafiar outros jogadores.
            </p>
            <div className={styles.createArrow}>→</div>
          </div>
        </div>

        {/* Stats */}
        <div className={styles.stats}>
          <div className={styles.statCard}>
            <span className={styles.statLabel}>Rank</span>
            <span className={styles.statValue}>—</span>
          </div>
          <div className={styles.statCard}>
            <span className={styles.statLabel}>Pontos</span>
            <span className={styles.statValue}>0</span>
          </div>
          <div className={styles.statCard}>
            <span className={styles.statLabel}>Win %</span>
            <span className={styles.statValue}>—</span>
          </div>
        </div>

        <p className={styles.greeting}>
          Bem-vindo, <strong>{user?.nome?.split(' ')[0] ?? 'Jogador'}</strong>!
        </p>
      </div>
    </div>
  );
}
