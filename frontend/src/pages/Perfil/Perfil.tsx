import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';
import styles from './Perfil.module.scss';

export function Perfil() {
  const { user, isAdmin, logout } = useAuth();
  const navigate = useNavigate();

  function handleLogout() {
    logout();
    navigate('/login', { replace: true });
  }

  const initials = user?.nome
    ? user.nome.split(' ').map(n => n[0]).slice(0, 2).join('').toUpperCase()
    : '?';

  return (
    <div className={styles.page}>
      <div className={styles.inner}>
        <h1 className={styles.title}>Meu Perfil</h1>

        <div className={styles.card}>
          <div className={styles.avatarRow}>
            <div className={styles.avatar}>{initials}</div>
            <div className={styles.userInfo}>
              <p className={styles.userName}>{user?.nome}</p>
              <p className={styles.userEmail}>{user?.email}</p>
              <span className={`${styles.roleBadge} ${isAdmin ? styles['roleBadge--admin'] : styles['roleBadge--user']}`}>
                {isAdmin ? 'Administrador' : 'Jogador'}
              </span>
            </div>
          </div>

          <div className={styles.divider} />

          <div className={styles.fields}>
            <div className={styles.field}>
              <span className={styles.label}>Nome</span>
              <span className={styles.value}>{user?.nome}</span>
            </div>
            <div className={styles.field}>
              <span className={styles.label}>E-mail</span>
              <span className={styles.value}>{user?.email}</span>
            </div>
            <div className={styles.field}>
              <span className={styles.label}>Função</span>
              <span className={styles.value}>{isAdmin ? 'Administrador' : 'Jogador'}</span>
            </div>
          </div>
        </div>

        <button className={styles.logoutBtn} onClick={handleLogout}>
          <IconLogout />
          Sair da conta
        </button>
      </div>
    </div>
  );
}

function IconLogout() {
  return (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor">
      <path d="M17 7l-1.41 1.41L18.17 11H8v2h10.17l-2.58 2.58L17 17l5-5-5-5zM4 5h8V3H4c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h8v-2H4V5z" />
    </svg>
  );
}
