import { Link, Outlet } from 'react-router-dom';
import { useAuth } from '../../../hooks/useAuth';
import { Button } from '../../ui/Button/Button';
import styles from './AppShell.module.scss';

export function AppShell() {
  const { user, logout, isAdmin } = useAuth();

  return (
    <div className={styles.shell}>
      <nav className={styles.nav}>
        <Link to="/" className={styles.brand}>DeuBranco</Link>
        <div className={styles.navRight}>
          {isAdmin && (
            <Link to="/admin/perguntas">
              <Button variant="ghost" size="sm">Admin</Button>
            </Link>
          )}
          <span className={styles.userName}>{user?.nome}</span>
          <Button variant="secondary" size="sm" onClick={logout}>Sair</Button>
        </div>
      </nav>
      <main className={styles.main}>
        <Outlet />
      </main>
    </div>
  );
}
