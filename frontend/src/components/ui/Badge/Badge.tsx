import type { PartidaStatus } from '../../../types/partida';
import styles from './Badge.module.scss';

type BadgeVariant = PartidaStatus | 'admin' | 'jogador';

interface BadgeProps {
  variant: BadgeVariant;
  children: React.ReactNode;
}

const LABELS: Record<BadgeVariant, string> = {
  AGUARDANDO: 'Aguardando',
  ANDAMENTO: 'Em andamento',
  FINALIZADA: 'Finalizada',
  CANCELADA: 'Cancelada',
  admin: 'Admin',
  jogador: 'Jogador',
};

export function Badge({ variant, children }: BadgeProps) {
  return (
    <span className={`${styles.badge} ${styles[`badge--${variant.toLowerCase()}`]}`}>
      {children}
    </span>
  );
}

export function StatusBadge({ status }: { status: PartidaStatus }) {
  return <Badge variant={status}>{LABELS[status]}</Badge>;
}
