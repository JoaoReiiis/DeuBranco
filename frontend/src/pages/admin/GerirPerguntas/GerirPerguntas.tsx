import { useState, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import questaoService from '../../../services/questaoService';
import type { QuestaoResponse } from '../../../types/questao';
import { DISCIPLINAS, INSTITUICOES } from '../../../types/questao';
import { Card } from '../../../components/ui/Card/Card';
import { Button } from '../../../components/ui/Button/Button';
import { Select } from '../../../components/ui/Select/Select';
import styles from './GerirPerguntas.module.scss';

const ATIVO_OPTIONS = [
  { value: '', label: 'Todas' },
  { value: 'true', label: 'Ativas' },
  { value: 'false', label: 'Inativas' },
];

export function GerirPerguntas() {
  const navigate = useNavigate();
  const [questoes, setQuestoes] = useState<QuestaoResponse[]>([]);
  const [total, setTotal] = useState(0);
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(false);
  const [disciplina, setDisciplina] = useState('');
  const [instituicao, setInstituicao] = useState('');
  const [ativo, setAtivo] = useState('');
  const [deleteId, setDeleteId] = useState<number | null>(null);

  const load = useCallback(async () => {
    setLoading(true);
    try {
      const params: Record<string, string | number | boolean> = { page, size: 15 };
      if (disciplina) params.disciplina = disciplina;
      if (instituicao) params.instituicao = instituicao;
      if (ativo !== '') params.ativo = ativo === 'true';
      const res = await questaoService.list(params as Parameters<typeof questaoService.list>[0]);
      setQuestoes(res.content);
      setTotal(res.totalPages);
    } finally {
      setLoading(false);
    }
  }, [page, disciplina, instituicao, ativo]);

  useEffect(() => { load(); }, [load]);

  async function handleDelete() {
    if (deleteId == null) return;
    await questaoService.remove(deleteId);
    setDeleteId(null);
    load();
  }

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <h1 className={styles.title}>Perguntas</h1>
        <Button onClick={() => navigate('/admin/perguntas/nova')}>Nova pergunta</Button>
      </div>

      <div className={styles.filters}>
        <Select
          label="Disciplina"
          options={[{ value: '', label: 'Todas' }, ...DISCIPLINAS]}
          value={disciplina}
          onChange={(e) => { setDisciplina(e.target.value); setPage(0); }}
        />
        <Select
          label="Instituição"
          options={[{ value: '', label: 'Todas' }, ...INSTITUICOES]}
          value={instituicao}
          onChange={(e) => { setInstituicao(e.target.value); setPage(0); }}
        />
        <Select
          label="Status"
          options={ATIVO_OPTIONS}
          value={ativo}
          onChange={(e) => { setAtivo(e.target.value); setPage(0); }}
        />
        <Button variant="secondary" onClick={() => navigate('/admin/perguntas/busca')}>
          Buscar texto
        </Button>
      </div>

      <Card>
        {loading && <p className={styles.empty}>Carregando...</p>}
        {!loading && questoes.length === 0 && (
          <p className={styles.empty}>Nenhuma pergunta encontrada para os critérios informados.</p>
        )}
        <div className={styles.list}>
          {questoes.map((q) => (
            <div key={q.id} className={`${styles.questionRow} ${!q.ativo ? styles.inactive : ''}`}>
              <div className={styles.questionInfo}>
                <p className={styles.questionText}>{q.enunciado}</p>
                <div className={styles.questionMeta}>
                  <span>{q.disciplina.replace('_', ' ')}</span>
                  <span>·</span>
                  <span>{q.instituicao}</span>
                  {!q.ativo && <span>· Inativa</span>}
                </div>
              </div>
              <div className={styles.questionActions}>
                <Button size="sm" variant="secondary" onClick={() => navigate(`/admin/perguntas/${q.id}/editar`)}>
                  Editar
                </Button>
                <Button size="sm" variant="danger" onClick={() => setDeleteId(q.id)}>
                  Excluir
                </Button>
              </div>
            </div>
          ))}
        </div>
      </Card>

      {total > 1 && (
        <div className={styles.pagination}>
          <Button size="sm" variant="secondary" disabled={page === 0} onClick={() => setPage(p => p - 1)}>
            Anterior
          </Button>
          <span>Página {page + 1} de {total}</span>
          <Button size="sm" variant="secondary" disabled={page >= total - 1} onClick={() => setPage(p => p + 1)}>
            Próxima
          </Button>
        </div>
      )}

      {deleteId != null && (
        <div className={styles.confirmOverlay} onClick={() => setDeleteId(null)}>
          <Card className={styles.confirmModal} onClick={(e) => e.stopPropagation()}>
            <h3 className={styles.confirmTitle}>Excluir pergunta?</h3>
            <p className={styles.confirmText}>
              A pergunta será desativada logicamente. Esta ação pode ser revertida alterando o status.
            </p>
            <div className={styles.confirmActions}>
              <Button variant="secondary" onClick={() => setDeleteId(null)}>Cancelar</Button>
              <Button variant="danger" onClick={handleDelete}>Excluir</Button>
            </div>
          </Card>
        </div>
      )}
    </div>
  );
}
