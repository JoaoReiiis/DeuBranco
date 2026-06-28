import logoHorizontal from '../../../assets/logo.png';
import logoVertical from '../../../assets/logo_vertical.png';
import styles from './Logo.module.scss';

interface LogoProps {
  variant?: 'horizontal' | 'vertical';
  height?: number;
}

export function Logo({ variant = 'horizontal', height = 36 }: LogoProps) {
  const src = variant === 'vertical' ? logoVertical : logoHorizontal;
  const alt = 'Deu Branco';

  return (
    <img
      src={src}
      alt={alt}
      height={height}
      className={styles.logo}
      draggable={false}
    />
  );
}
