import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';
import { Card } from '../../components/ui/Card/Card';
import { Input } from '../../components/ui/Input/Input';
import { Button } from '../../components/ui/Button/Button';
import partidaService from '../../services/partidaService';
import styles from './Home.module.scss';

export function Home() {
  const { user, isAdmin } = useAuth();
  const navigate = useNavigate();
  const [showPinModal, setShowPinModal] = useState(false);
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

  const firstName = user?.nome.split(' ')[0] ?? 'Jogador';

  return (
    <div className={styles.page}>
      <div className={styles.hero}>
        <h1 className={styles.greeting}>Olá, {firstName}.</h1>
        <p className={styles.subtitle}>O que vamos estudar hoje?</p>
      </div>

      <div className={styles.section}>
        <span className={styles.sectionTitle}>Partidas</span>
        <div className={styles.actions}>
          <Card className={styles.actionCard} onClick={() => navigate('/match/configurar')}>
            <span className={styles.actionIcon}>⚡</span>
            <span className={styles.actionTitle}>Criar sala</span>
            <span className={styles.actionDesc}>Inicie uma nova partida</span>
          </Card>
          <Card className={styles.actionCard} onClick={() => setShowPinModal(true)}>
            <span className={styles.actionIcon}>🔑</span>
            <span className={styles.actionTitle}>Entrar com PIN</span>
            <span className={styles.actionDesc}>Junte-se a uma sala</span>
          </Card>
        </div>
      </div>

      <div className={styles.section}>
        <span className={styles.sectionTitle}>Histórico</span>
        <Button variant="secondary" onClick={() => navigate('/historico')}>
          Ver partidas anteriores
        </Button>
      </div>

      {isAdmin && (
        <div className={styles.section}>
          <span className={styles.sectionTitle}>Administração</span>
          <Button variant="ghost" onClick={() => navigate('/admin/perguntas')}>
            Gerenciar perguntas
          </Button>
        </div>
      )}

      {showPinModal && (
        <div className={styles.overlay} onClick={() => setShowPinModal(false)}>
          <Card className={styles.modal} onClick={(e) => e.stopPropagation()}>
            <h3 className={styles.modalTitle}>Entrar em uma sala</h3>
            <Input
              label="Código PIN"
              value={pin}
              onChange={(e) => setPin(e.target.value.toUpperCase())}
              placeholder="Ex: ABC123"
              error={pinError}
              maxLength={10}
              autoFocus
              onKeyDown={(e) => e.key === 'Enter' && handleJoinByPin()}
            />
            <div className={styles.modalActions}>
              <Button variant="secondary" onClick={() => { setShowPinModal(false); setPin(''); setPinError(''); }}>
                Cancelar
              </Button>
              <Button onClick={handleJoinByPin} disabled={loading}>
                {loading ? 'Entrando...' : 'Entrar'}
              </Button>
            </div>
          </Card>
        </div>
      )}
    </div>
  );
}
