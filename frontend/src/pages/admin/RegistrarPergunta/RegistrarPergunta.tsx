import { useState, type FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import questaoService from '../../../services/questaoService';
import { DISCIPLINAS, INSTITUICOES, type QuestaoRequest, type Alternativa, type Disciplina, type Instituicao } from '../../../types/questao';
import { Card } from '../../../components/ui/Card/Card';
import { Input, TextArea } from '../../../components/ui/Input/Input';
import { Select } from '../../../components/ui/Select/Select';
import { Button } from '../../../components/ui/Button/Button';
import styles from './RegistrarPergunta.module.scss';

const ALTERNATIVAS = [
  { value: 'A', label: 'A' },
  { value: 'B', label: 'B' },
  { value: 'C', label: 'C' },
  { value: 'D', label: 'D' },
  { value: 'E', label: 'E' },
];

const EMPTY: QuestaoRequest = {
  enunciado: '', opcaoA: '', opcaoB: '', opcaoC: '', opcaoD: '', opcaoE: '',
  alternativaCorreta: 'A', disciplina: 'ENEM' as unknown as Disciplina,
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
      setError('Erro ao salvar a pergunta. Verifique os campos obrigatórios.');
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <button className={styles.back} onClick={() => navigate('/admin/perguntas')}>←</button>
        <h1 className={styles.title}>Nova pergunta</h1>
      </div>
      <Card>
        <form className={styles.form} onSubmit={handleSubmit}>
          <TextArea
            id="enunciado"
            label="Enunciado"
            value={form.enunciado}
            onChange={(e) => set('enunciado', e.target.value)}
            placeholder="Digite o enunciado da questão..."
            required
          />
          <div className={styles.section}>
            <span className={styles.sectionTitle}>Alternativas</span>
            {(['A', 'B', 'C', 'D', 'E'] as Alternativa[]).map((l) => (
              <Input
                key={l}
                label={`Opção ${l}`}
                value={form[`opcao${l}` as keyof QuestaoRequest] as string}
                onChange={(e) => set(`opcao${l}` as keyof QuestaoRequest, e.target.value)}
                placeholder={`Texto da alternativa ${l}`}
                required
              />
            ))}
          </div>
          <div className={styles.row}>
            <Select
              label="Resposta correta"
              options={ALTERNATIVAS}
              value={form.alternativaCorreta}
              onChange={(e) => set('alternativaCorreta', e.target.value)}
              required
            />
            <Select
              label="Disciplina"
              options={DISCIPLINAS}
              value={form.disciplina}
              onChange={(e) => set('disciplina', e.target.value)}
              placeholder="Selecione..."
              required
            />
          </div>
          <Select
            label="Instituição"
            options={INSTITUICOES}
            value={form.instituicao}
            onChange={(e) => set('instituicao', e.target.value)}
            placeholder="Selecione..."
            required
          />
          {success && <p className={styles.success}>Pergunta salva com sucesso!</p>}
          {error && <p className={styles.error}>{error}</p>}
          <div className={styles.actions}>
            <Button type="button" variant="secondary" onClick={() => navigate('/admin/perguntas')}>
              Cancelar
            </Button>
            <Button type="submit" disabled={loading}>
              {loading ? 'Salvando...' : 'Salvar'}
            </Button>
          </div>
        </form>
      </Card>
    </div>
  );
}
