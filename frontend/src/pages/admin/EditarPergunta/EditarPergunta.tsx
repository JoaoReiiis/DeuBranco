import { useState, useEffect, type FormEvent } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import questaoService from '../../../services/questaoService';
import { DISCIPLINAS, INSTITUICOES, type QuestaoRequest, type Alternativa } from '../../../types/questao';
import { Button } from '../../../components/ui/Button/Button';
import styles from './EditarPergunta.module.scss';

const OPCOES: Alternativa[] = ['A', 'B', 'C', 'D'];

export function EditarPergunta() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [form, setForm] = useState<QuestaoRequest | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);
  const [showDelete, setShowDelete] = useState(false);

  useEffect(() => {
    if (!id) return;
    questaoService.getById(Number(id)).then((q) => {
      setForm({
        enunciado: q.enunciado,
        opcaoA: q.opcaoA, opcaoB: q.opcaoB, opcaoC: q.opcaoC,
        opcaoD: q.opcaoD, opcaoE: q.opcaoE,
        alternativaCorreta: q.alternativaCorreta,
        disciplina: q.disciplina,
        instituicao: q.instituicao,
        imagens: q.imagens ?? undefined,
        ativo: q.ativo,
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
      setError('Erro ao atualizar a questão.');
    } finally {
      setLoading(false);
    }
  }

  async function handleDelete() {
    if (!id) return;
    await questaoService.remove(Number(id));
    navigate('/admin/perguntas');
  }

  if (!form) return <p className={styles.loading}>Carregando...</p>;

  return (
    <div className={styles.page}>
      <div className={styles.inner}>
        {/* Back link */}
        <button className={styles.backLink} onClick={() => navigate('/admin/perguntas')}>
          ← voltar ao banco de questoes
        </button>

        <div className={styles.titleRow}>
          <h1 className={styles.title}>Editar Questao</h1>
          <span className={styles.idBadge}>ID: {id}</span>
        </div>

        <div className={styles.formCard}>
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
                rows={5}
                required
              />
            </div>

            {/* Answer Options */}
            <div className={styles.field}>
              <label className={styles.label}>Answer Options</label>
              <div className={styles.answerList}>
                {OPCOES.map((l) => {
                  const fieldKey = `opcao${l}` as keyof QuestaoRequest;
                  const isCorrect = form.alternativaCorreta === l;
                  return (
                    <div key={l} className={`${styles.answerRow} ${isCorrect ? styles['answerRow--correct'] : ''}`}>
                      <input
                        type="radio"
                        name="correta"
                        checked={isCorrect}
                        onChange={() => set('alternativaCorreta', l)}
                        className={styles.answerRadio}
                      />
                      <input
                        className={styles.answerInput}
                        value={form[fieldKey] as string}
                        onChange={(e) => set(fieldKey, e.target.value)}
                        placeholder={`Alternativa ${l}`}
                      />
                      {isCorrect && <span className={styles.correctBadge}>Correct Answer</span>}
                    </div>
                  );
                })}
              </div>
            </div>

            {success && <p className={styles.success}>Questão atualizada!</p>}
            {error && <p className={styles.error}>{error}</p>}

            {/* Bottom actions */}
            <div className={styles.actions}>
              <button
                type="button"
                className={styles.deleteLink}
                onClick={() => setShowDelete(true)}
              >
                🗑 Delete Question
              </button>
              <div className={styles.rightActions}>
                <Button type="button" variant="secondary" onClick={() => navigate('/admin/perguntas')}>
                  Cancel
                </Button>
                <Button type="submit" disabled={loading}>
                  {loading ? 'Salvando...' : 'Update Question'}
                </Button>
              </div>
            </div>
          </form>
        </div>
      </div>

      {/* Delete confirmation modal */}
      {showDelete && (
        <div className={styles.overlay} onClick={() => setShowDelete(false)}>
          <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
            <div className={styles.modalIcon}>🗑</div>
            <h3 className={styles.modalTitle}>Tem certeza que deseja excluir esta questão?</h3>
            <p className={styles.modalDesc}>
              Esta ação não pode ser desfeita. A questão será removida permanentemente do banco de dados.
            </p>
            <div className={styles.modalActions}>
              <button className={styles.cancelBtn} onClick={() => setShowDelete(false)}>Cancelar</button>
              <button className={styles.confirmBtn} onClick={handleDelete}>Excluir</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
