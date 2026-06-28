import { Outlet } from 'react-router-dom';
import styles from './AuthShell.module.scss';

export function AuthShell() {
  return (
    <div className={styles.page}>
      <div className={styles.container}>
        <div className={styles.logo}>
          <h1>DeuBranco</h1>
          <p>Plataforma de estudos competitivos</p>
        </div>
        <Outlet />
      </div>
    </div>
  );
}
