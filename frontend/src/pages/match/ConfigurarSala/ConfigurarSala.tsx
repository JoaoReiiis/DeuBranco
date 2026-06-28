import { useState, type FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import partidaService from '../../../services/partidaService';
import { DISCIPLINAS, type Disciplina } from '../../../types/questao';
import { Button } from '../../../components/ui/Button/Button';
import styles from './ConfigurarSala.module.scss';

const TEMPOS = [10, 20, 30, 60];

export function ConfigurarSala() {
  const navigate = useNavigate();
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
    setError('');
    setLoading(true);
    try {
      const partida = await partidaService.create({ numeroQuestoes: 10, disciplinas, tempoDeJogo });
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
      <div className={styles.inner}>
        <div className={styles.header}>
          <h1 className={styles.title}>Criar Arena</h1>
          <p className={styles.subtitle}>Configure sua sala de estudos competitivos</p>
        </div>

        <form className={styles.form} onSubmit={handleSubmit}>
          {/* Time selector */}
          <section className={styles.section}>
            <h2 className={styles.sectionTitle}>Tempo por questão</h2>
            <div className={styles.timeGrid}>
              {TEMPOS.map((t) => (
                <button
                  key={t}
                  type="button"
                  className={`${styles.timeBtn} ${tempoDeJogo === t ? styles['timeBtn--active'] : ''}`}
                  onClick={() => setTempoDeJogo(t)}
                >
                  {t}s
                </button>
              ))}
            </div>
          </section>

          {/* Discipline grid */}
          <section className={styles.section}>
            <h2 className={styles.sectionTitle}>Disciplinas</h2>
            <div className={styles.disciplinaGrid}>
              {DISCIPLINAS.map(({ value, label }) => {
                const checked = disciplinas.includes(value);
                return (
                  <label key={value} className={`${styles.disciplinaItem} ${checked ? styles['disciplinaItem--checked'] : ''}`}>
                    <input
                      type="checkbox"
                      checked={checked}
                      onChange={() => toggleDisciplina(value)}
                      className={styles.disciplinaCheckbox}
                    />
                    <span className={styles.disciplinaLabel}>{label}</span>
                  </label>
                );
              })}
            </div>
          </section>

          {error && <p className={styles.error}>{error}</p>}

          <Button type="submit" fullWidth disabled={loading}>
            {loading ? 'Criando...' : 'Iniciar Arena →'}
          </Button>
        </form>
      </div>
    </div>
  );
}
