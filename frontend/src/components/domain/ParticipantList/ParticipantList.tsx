import type { ParticipacaoResponse } from '../../../types/partida';
import styles from './ParticipantList.module.scss';

interface ParticipantListProps {
  participants: ParticipacaoResponse[];
  hostId: number;
}

export function ParticipantList({ participants, hostId }: ParticipantListProps) {
  return (
    <div className={styles.list}>
      {participants.map((p) => (
        <div key={p.id} className={styles.item}>
          <div className={styles.avatar}>
            {p.jogador.nome.charAt(0).toUpperCase()}
          </div>
          <span className={styles.name}>{p.jogador.nome}</span>
          {p.jogador.id === hostId && (
            <span className={styles.host}>Host</span>
          )}
        </div>
      ))}
    </div>
  );
}
