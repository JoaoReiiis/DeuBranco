import { Link, NavLink, Outlet, useNavigate } from 'react-router-dom';
import { useAuth } from '../../../hooks/useAuth';
import { Logo } from '../../ui/Logo/Logo';
import styles from './AppShell.module.scss';

function IconHome() {
  return (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
      <path d="M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z" />
    </svg>
  );
}

function IconHistory() {
  return (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
      <path d="M13 3a9 9 0 0 0-9 9H1l3.89 3.89.07.14L9 12H6c0-3.87 3.13-7 7-7s7 3.13 7 7-3.13 7-7 7c-1.93 0-3.68-.79-4.94-2.06l-1.42 1.42A8.954 8.954 0 0 0 13 21a9 9 0 0 0 0-18zm-1 5v5l4.28 2.54.72-1.21-3.5-2.08V8H12z" />
    </svg>
  );
}

function IconPerson() {
  return (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
      <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z" />
    </svg>
  );
}

function IconGear() {
  return (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
      <path d="M19.14 12.94c.04-.3.06-.61.06-.94s-.02-.64-.07-.94l2.03-1.58a.49.49 0 0 0 .12-.61l-1.92-3.32a.488.488 0 0 0-.59-.22l-2.39.96c-.5-.38-1.03-.7-1.62-.94l-.36-2.54a.484.484 0 0 0-.48-.41h-3.84c-.24 0-.43.17-.47.41l-.36 2.54c-.59.24-1.13.57-1.62.94l-2.39-.96c-.22-.08-.47 0-.59.22L2.74 8.87c-.12.21-.08.47.12.61l2.03 1.58c-.05.3-.09.63-.09.94s.02.64.07.94l-2.03 1.58a.49.49 0 0 0-.12.61l1.92 3.32c.12.22.37.29.59.22l2.39-.96c.5.38 1.03.7 1.62.94l.36 2.54c.05.24.24.41.48.41h3.84c.24 0 .44-.17.47-.41l.36-2.54c.59-.24 1.13-.56 1.62-.94l2.39.96c.22.08.47 0 .59-.22l1.92-3.32a.49.49 0 0 0-.12-.61l-2.01-1.58zM12 15.6c-1.98 0-3.6-1.62-3.6-3.6s1.62-3.6 3.6-3.6 3.6 1.62 3.6 3.6-1.62 3.6-3.6 3.6z" />
    </svg>
  );
}

function IconTrophy() {
  return (
    <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor">
      <path d="M19 5h-2V3H7v2H5c-1.1 0-2 .9-2 2v1c0 2.55 1.92 4.63 4.39 4.94.63 1.5 1.98 2.63 3.61 2.96V19H7v2h10v-2h-4v-3.1a5.002 5.002 0 0 0 3.61-2.96C19.08 12.63 21 10.55 21 8V7c0-1.1-.9-2-2-2zM5 8V7h2v3.82C5.84 10.4 5 9.3 5 8zm14 0c0 1.3-.84 2.4-2 2.82V7h2v1z" />
    </svg>
  );
}

function IconLogout() {
  return (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor">
      <path d="M17 7l-1.41 1.41L18.17 11H8v2h10.17l-2.58 2.58L17 17l5-5-5-5zM4 5h8V3H4c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h8v-2H4V5z" />
    </svg>
  );
}

export function AppShell() {
  const { user, isAdmin, logout } = useAuth();
  const navigate = useNavigate();

  function handleLogout() {
    logout();
    navigate('/login', { replace: true });
  }

  return (
    <div className={styles.shell}>
      <header className={styles.header}>
        <div className={styles.headerInner}>
          <Link to="/" className={styles.logoBox}>
            <Logo variant="horizontal" height={32} />
          </Link>

          <nav className={styles.nav}>
            <NavLink
              to="/"
              end
              className={({ isActive }) => `${styles.navItem} ${isActive ? styles['navItem--active'] : ''}`}
            >
              <IconHome />
              <span>Início</span>
            </NavLink>
            <NavLink
              to="/historico"
              className={({ isActive }) => `${styles.navItem} ${isActive ? styles['navItem--active'] : ''}`}
            >
              <IconHistory />
              <span>Histórico</span>
            </NavLink>
            <NavLink
              to="/perfil"
              className={({ isActive }) => `${styles.navItem} ${isActive ? styles['navItem--active'] : ''}`}
            >
              <IconPerson />
              <span>Perfil</span>
            </NavLink>
            {isAdmin && (
              <NavLink
                to="/admin/perguntas"
                className={({ isActive }) => `${styles.navItem} ${isActive ? styles['navItem--active'] : ''}`}
              >
                <IconGear />
                <span>Questoes</span>
              </NavLink>
            )}
          </nav>

          <div className={styles.userActions}>
            {isAdmin ? (
              <span className={`${styles.badge} ${styles['badge--admin']}`}>
                <IconTrophy />
                Administrador
              </span>
            ) : (
              <span className={`${styles.badge} ${styles['badge--user']}`}>
                <IconTrophy />
                {user?.nome?.split(' ')[0] ?? 'Jogador'}
              </span>
            )}
            <button
              className={styles.logoutBtn}
              onClick={handleLogout}
              title="Sair da conta"
            >
              <IconLogout />
              <span>Sair</span>
            </button>
          </div>
        </div>
      </header>

      <main className={styles.main}>
        <Outlet />
      </main>

      <footer className={styles.footer}>
        <div className={styles.footerInner}>
          <span className={styles.footerBrand}>Deu Branco</span>
          <span className={styles.footerCopy}>© 2026 Deu Branco. Todos os direitos reservados.</span>
        </div>
      </footer>
    </div>
  );
}
