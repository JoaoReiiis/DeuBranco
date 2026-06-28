import { useState, type FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import questaoService from '../../../services/questaoService';
import { DISCIPLINAS, INSTITUICOES, type QuestaoRequest, type Alternativa, type Disciplina, type Instituicao } from '../../../types/questao';
import { Button } from '../../../components/ui/Button/Button';
import styles from './RegistrarPergunta.module.scss';

const OPCOES: Alternativa[] = ['A', 'B', 'C', 'D', 'E'];

const EMPTY: QuestaoRequest = {
  enunciado: '',
  opcaoA: '', opcaoB: '', opcaoC: '', opcaoD: '', opcaoE: '',
  alternativaCorreta: 'A',
  disciplina: 'ENEM' as unknown as Disciplina,
  instituicao: 'ENEM' as Instituicao,
};

export function RegistrarPergunta() {
  const navigate = useNavigate();
  const [form, setForm] = useState<QuestaoRequest>({ ...EMPTY });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);

  function set(field: keyof QuestaoRequest, value: string) {
    setForm(prev => ({ ...prev, [field]: value }));
  }

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      await questaoService.create(form);
      setSuccess(true);
      setForm({ ...EMPTY });
      setTimeout(() => setSuccess(false), 3000);
    } catch {
      setError('Erro ao salvar a questão. Verifique os campos obrigatórios.');
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className={styles.page}>
      <div className={styles.inner}>
        <div className={styles.formCard}>
          <div className={styles.cardHeader}>
            <h1 className={styles.title}>Add New Question</h1>
            <p className={styles.subtitle}>
              Contribua com o banco de questões adicionando uma nova questão de múltipla escolha.
            </p>
          </div>

          <form className={styles.form} onSubmit={handleSubmit}>
            {/* Disciplina + Instituição */}
            <div className={styles.row}>
              <div className={styles.field}>
                <label className={styles.label}>Disciplina</label>
                <select
                  className={styles.select}
                  value={form.disciplina}
                  onChange={(e) => set('disciplina', e.target.value)}
                  required
                >
                  <option value="">Selecionar Categoria</option>
                  {DISCIPLINAS.map(d => (
                    <option key={d.value} value={d.value}>{d.label}</option>
                  ))}
                </select>
              </div>
              <div className={styles.field}>
                <label className={styles.label}>Instituição</label>
                <select
                  className={styles.select}
                  value={form.instituicao}
                  onChange={(e) => set('instituicao', e.target.value)}
                  required
                >
                  <option value="">Selecionar instituição</option>
                  {INSTITUICOES.map(i => (
                    <option key={i.value} value={i.value}>{i.label}</option>
                  ))}
                </select>
              </div>
            </div>

            {/* Enunciado */}
            <div className={styles.field}>
              <label className={styles.label}>Enunciado</label>
              <textarea
                className={styles.textarea}
                value={form.enunciado}
                onChange={(e) => set('enunciado', e.target.value)}
                placeholder="Adicione o enunciado da questao"
                rows={4}
                required
              />
            </div>

            {/* Answers */}
            <div className={styles.field}>
              <label className={styles.label}>Answers (Mark the correct one)</label>
              <div className={styles.answerList}>
                {OPCOES.map((l) => {
                  const fieldKey = `opcao${l}` as keyof QuestaoRequest;
                  const isCorrect = form.alternativaCorreta === l;
                  return (
                    <div key={l} className={`${styles.answerRow} ${isCorrect ? styles['answerRow--correct'] : ''}`}>
                      <span className={styles.answerLetter}>{l}</span>
                      <input
                        className={styles.answerInput}
                        value={form[fieldKey] as string}
                        onChange={(e) => set(fieldKey, e.target.value)}
                        placeholder={`Texto da alternativa ${l}...`}
                      />
                      <input
                        type="radio"
                        name="correta"
                        checked={isCorrect}
                        onChange={() => set('alternativaCorreta', l)}
                        className={styles.answerRadio}
                      />
                      {isCorrect && <span className={styles.correctBadge}>Correct Answer</span>}
                    </div>
                  );
                })}
              </div>
            </div>

            {success && <p className={styles.success}>Questão salva com sucesso!</p>}
            {error && <p className={styles.error}>{error}</p>}

            <div className={styles.actions}>
              <Button type="button" variant="secondary" onClick={() => navigate('/admin/perguntas')}>
                Cancel
              </Button>
              <Button type="submit" disabled={loading}>
                {loading ? 'Salvando...' : 'Save Question'}
              </Button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}
