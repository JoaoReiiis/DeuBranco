import { Outlet } from 'react-router-dom';
import styles from './AuthShell.module.scss';

export function AuthShell() {
  return (
    <div className={styles.page}>
      <Outlet />
    </div>
  );
}
