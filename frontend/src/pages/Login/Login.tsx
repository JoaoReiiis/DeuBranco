import { useState, type FormEvent } from 'react';
import { useNavigate, useLocation, Link } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';
import { Logo } from '../../components/ui/Logo/Logo';
import { Input } from '../../components/ui/Input/Input';
import { Button } from '../../components/ui/Button/Button';
import styles from './Login.module.scss';

export function Login() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const from = (location.state as { from?: { pathname: string } })?.from?.pathname ?? '/';

  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      await login(email, senha);
      navigate(from, { replace: true });
    } catch {
      setError('E-mail ou senha inválidos. Verifique suas credenciais.');
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className={styles.card}>
      <div className={styles.logoArea}>
        <p className={styles.welcomeText}>Bem Vindo ao</p>
        <Logo variant="horizontal" height={56} />
      </div>

      <form className={styles.form} onSubmit={handleSubmit} noValidate>
        <Input
          id="email"
          label="E-mail"
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          placeholder="seu@email.com"
          required
          autoComplete="email"
        />
        <Input
          id="senha"
          label="Senha"
          type="password"
          value={senha}
          onChange={(e) => setSenha(e.target.value)}
          placeholder="••••••••"
          required
          autoComplete="current-password"
        />
        {error && <p className={styles.error}>{error}</p>}
        <Button type="submit" fullWidth disabled={loading}>
          {loading ? 'Entrando...' : 'Login →'}
        </Button>
      </form>

      <div className={styles.links}>
        <a href="#" className={styles.link}>Esqueceu sua senha?</a>
        <p className={styles.linkText}>
          Não tem uma conta?{' '}
          <Link to="/register" className={styles.link}>Criar conta</Link>
        </p>
      </div>
    </div>
  );
}
