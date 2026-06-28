import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import partidaService from '../../../services/partidaService';
import type { HistoricoPartidaResponse } from '../../../types/partida';
import { Card } from '../../../components/ui/Card/Card';
import { StatusBadge } from '../../../components/ui/Badge/Badge';
import styles from './HistoricoPartidas.module.scss';

export function HistoricoPartidas() {
  const navigate = useNavigate();
  const [historico, setHistorico] = useState<HistoricoPartidaResponse[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    partidaService.getHistory().then(setHistorico).finally(() => setLoading(false));
  }, []);

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <button className={styles.back} onClick={() => navigate('/')}>←</button>
        <h1 className={styles.title}>Histórico</h1>
      </div>
      <Card>
        {loading && <p className={styles.empty}>Carregando...</p>}
        {!loading && historico.length === 0 && (
          <p className={styles.empty}>Você ainda não participou de nenhuma partida.</p>
        )}
        <div className={styles.list}>
          {historico.map((h) => (
            <div key={h.partidaId} className={styles.row}>
              <div>
                <div className={styles.pin}>{h.codigoPin}</div>
                <div className={styles.date}>{new Date(h.criadoEm).toLocaleDateString('pt-BR')}</div>
              </div>
              <StatusBadge status={h.statusSala} />
              <span className={styles.score}>{h.scorePartida} pts</span>
            </div>
          ))}
        </div>
      </Card>
    </div>
  );
}
