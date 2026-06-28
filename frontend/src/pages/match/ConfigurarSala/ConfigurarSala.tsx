import { useState, type FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import partidaService from '../../../services/partidaService';
import { DISCIPLINAS, type Disciplina } from '../../../types/questao';
import { Card } from '../../../components/ui/Card/Card';
import { Input } from '../../../components/ui/Input/Input';
import { Button } from '../../../components/ui/Button/Button';
import styles from './ConfigurarSala.module.scss';

export function ConfigurarSala() {
  const navigate = useNavigate();
  const [numeroQuestoes, setNumeroQuestoes] = useState(10);
  const [tempoDeJogo, setTempoDeJogo] = useState(30);
  const [disciplinas, setDisciplinas] = useState<Disciplina[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  function toggleDisciplina(d: Disciplina) {
    setDisciplinas(prev =>
      prev.includes(d) ? prev.filter(x => x !== d) : [...prev, d]
    );
  }

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    if (disciplinas.length === 0) {
      setError('Selecione pelo menos uma disciplina.');
      return;
    }
    if (numeroQuestoes < 5 || numeroQuestoes > 30) {
      setError('O número de questões deve ser entre 5 e 30.');
      return;
    }
    setError('');
    setLoading(true);
    try {
      const partida = await partidaService.create({ numeroQuestoes, disciplinas, tempoDeJogo });
      navigate(`/match/${partida.id}/lobby`);
    } catch (err: unknown) {
      const msg = (err as { response?: { data?: { message?: string } } })?.response?.data?.message;
      setError(msg ?? 'Não foi possível criar a sala. Verifique se há questões suficientes.');
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <button className={styles.back} onClick={() => navigate('/')}>←</button>
        <h1 className={styles.title}>Criar sala</h1>
      </div>
      <Card>
        <form className={styles.form} onSubmit={handleSubmit}>
          <div className={styles.section}>
            <span className={styles.sectionTitle}>Disciplinas</span>
            <div className={styles.disciplinasGrid}>
              {DISCIPLINAS.map(({ value, label }) => (
                <button
                  key={value}
                  type="button"
                  className={`${styles.disciplinaChip} ${disciplinas.includes(value) ? styles['disciplinaChip--selected'] : ''}`}
                  onClick={() => toggleDisciplina(value)}
                >
                  {label}
                </button>
              ))}
            </div>
          </div>
          <div className={styles.row}>
            <Input
              label="Nº de questões (5–30)"
              type="number"
              min={5}
              max={30}
              value={numeroQuestoes}
              onChange={(e) => setNumeroQuestoes(Number(e.target.value))}
              required
            />
            <Input
              label="Tempo por questão (seg)"
              type="number"
              min={10}
              max={300}
              value={tempoDeJogo}
              onChange={(e) => setTempoDeJogo(Number(e.target.value))}
              required
            />
          </div>
          {error && <p className={styles.error}>{error}</p>}
          <Button type="submit" fullWidth disabled={loading}>
            {loading ? 'Criando sala...' : 'Criar sala'}
          </Button>
        </form>
      </Card>
    </div>
  );
}
