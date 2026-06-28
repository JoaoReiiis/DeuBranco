import { useState, useEffect, type FormEvent } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import questaoService from '../../../services/questaoService';
import { DISCIPLINAS, INSTITUICOES, type QuestaoRequest, type Alternativa } from '../../../types/questao';
import { Card } from '../../../components/ui/Card/Card';
import { Input, TextArea } from '../../../components/ui/Input/Input';
import { Select } from '../../../components/ui/Select/Select';
import { Button } from '../../../components/ui/Button/Button';
import styles from '../RegistrarPergunta/RegistrarPergunta.module.scss';

const ALTERNATIVAS = [
  { value: 'A', label: 'A' }, { value: 'B', label: 'B' }, { value: 'C', label: 'C' },
  { value: 'D', label: 'D' }, { value: 'E', label: 'E' },
];

export function EditarPergunta() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [form, setForm] = useState<QuestaoRequest | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);

  useEffect(() => {
    if (!id) return;
    questaoService.getById(Number(id)).then((q) => {
      setForm({
        enunciado: q.enunciado, opcaoA: q.opcaoA, opcaoB: q.opcaoB,
        opcaoC: q.opcaoC, opcaoD: q.opcaoD, opcaoE: q.opcaoE,
        alternativaCorreta: q.alternativaCorreta, disciplina: q.disciplina,
        instituicao: q.instituicao, imagens: q.imagens ?? undefined, ativo: q.ativo,
      });
    });
  }, [id]);

  function set(field: keyof QuestaoRequest, value: string | boolean) {
    setForm(prev => prev ? { ...prev, [field]: value } : prev);
  }

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    if (!form || !id) return;
    setError('');
    setLoading(true);
    try {
      await questaoService.update(Number(id), form);
      setSuccess(true);
      setTimeout(() => { setSuccess(false); navigate('/admin/perguntas'); }, 1500);
    } catch {
      setError('Erro ao atualizar a pergunta.');
    } finally {
      setLoading(false);
    }
  }

  if (!form) return <p>Carregando...</p>;

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <button className={styles.back} onClick={() => navigate('/admin/perguntas')}>←</button>
        <h1 className={styles.title}>Editar pergunta</h1>
      </div>
      <Card>
        <form className={styles.form} onSubmit={handleSubmit}>
          <TextArea
            label="Enunciado"
            value={form.enunciado}
            onChange={(e) => set('enunciado', e.target.value)}
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
                required
              />
            ))}
          </div>
          <div className={styles.row}>
            <Select label="Resposta correta" options={ALTERNATIVAS} value={form.alternativaCorreta}
              onChange={(e) => set('alternativaCorreta', e.target.value)} required />
            <Select label="Disciplina" options={DISCIPLINAS} value={form.disciplina}
              onChange={(e) => set('disciplina', e.target.value)} required />
          </div>
          <Select label="Instituição" options={INSTITUICOES} value={form.instituicao}
            onChange={(e) => set('instituicao', e.target.value)} required />
          {success && <p className={styles.success}>Pergunta atualizada!</p>}
          {error && <p className={styles.error}>{error}</p>}
          <div className={styles.actions}>
            <Button type="button" variant="secondary" onClick={() => navigate('/admin/perguntas')}>Cancelar</Button>
            <Button type="submit" disabled={loading}>{loading ? 'Salvando...' : 'Atualizar'}</Button>
          </div>
        </form>
      </Card>
    </div>
  );
}
