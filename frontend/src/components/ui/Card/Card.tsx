import type { HTMLAttributes, ReactNode } from 'react';
import styles from './Card.module.scss';

type CardVariant = 'default' | 'soft' | 'flat';

interface CardProps extends HTMLAttributes<HTMLDivElement> {
  variant?: CardVariant;
  children: ReactNode;
}

export function Card({ variant = 'default', className = '', children, ...rest }: CardProps) {
  const classes = [
    styles.card,
    variant !== 'default' ? styles[`card--${variant}`] : '',
    className,
  ]
    .filter(Boolean)
    .join(' ');

  return (
    <div className={classes} {...rest}>
      {children}
    </div>
  );
}
