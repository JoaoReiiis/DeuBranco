import { useState, type FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import jogadorService from '../../services/jogadorService';
import { useAuth } from '../../hooks/useAuth';
import { Card } from '../../components/ui/Card/Card';
import { Input } from '../../components/ui/Input/Input';
import { Button } from '../../components/ui/Button/Button';
import styles from './Register.module.scss';

export function Register() {
  const { login } = useAuth();
  const navigate = useNavigate();

  const [nome, setNome] = useState('');
  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      await jogadorService.register({ nome, email, senha });
      await login(email, senha);
      navigate('/', { replace: true });
    } catch (err: unknown) {
      const msg = (err as { response?: { data?: { message?: string } } })?.response?.data?.message;
      setError(msg ?? 'Não foi possível criar a conta. Tente novamente.');
    } finally {
      setLoading(false);
    }
  }

  return (
    <Card>
      <div className={styles.card}>
        <h2 className={styles.title}>Criar conta</h2>
        <form className={styles.form} onSubmit={handleSubmit} noValidate>
          <Input
            id="nome"
            label="Nome"
            type="text"
            value={nome}
            onChange={(e) => setNome(e.target.value)}
            placeholder="Seu nome completo"
            required
            autoComplete="name"
          />
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
            autoComplete="new-password"
            minLength={6}
          />
          {error && <p className={styles.error}>{error}</p>}
          <Button type="submit" fullWidth disabled={loading}>
            {loading ? 'Criando conta...' : 'Cadastrar'}
          </Button>
        </form>
        <Button variant="secondary" fullWidth onClick={() => navigate('/login')}>
          Já tenho uma conta
        </Button>
      </div>
    </Card>
  );
}
