import { useState, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import questaoService from '../../../services/questaoService';
import type { QuestaoResponse } from '../../../types/questao';
import { Button } from '../../../components/ui/Button/Button';
import styles from './GerirPerguntas.module.scss';

export function GerirPerguntas() {
  const navigate = useNavigate();
  const [questoes, setQuestoes] = useState<QuestaoResponse[]>([]);
  const [total, setTotal] = useState(0);
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(false);
  const [search, setSearch] = useState('');
  const [deleteId, setDeleteId] = useState<number | null>(null);

  const load = useCallback(async () => {
    setLoading(true);
    try {
      const params: Record<string, string | number> = { page, size: 9 };
      if (search) params.enunciado = search;
      const res = await questaoService.list(params as Parameters<typeof questaoService.list>[0]);
      setQuestoes(res.content);
      setTotal(res.totalPages);
    } finally {
      setLoading(false);
    }
  }, [page, search]);

  useEffect(() => { load(); }, [load]);

  async function handleDelete() {
    if (deleteId == null) return;
    await questaoService.remove(deleteId);
    setDeleteId(null);
    load();
  }

  return (
    <div className={styles.page}>
      <div className={styles.inner}>
        {/* Header */}
        <div className={styles.header}>
          <div>
            <h1 className={styles.title}>Banco de Questões</h1>
            <p className={styles.subtitle}>Gerencie e organize as questões do sistema</p>
          </div>
          <Button onClick={() => navigate('/admin/perguntas/nova')}>
            + Nova Questão
          </Button>
        </div>

        {/* Search */}
        <div className={styles.controls}>
          <div className={styles.searchBox}>
            <span className={styles.searchIcon}>🔍</span>
            <input
              className={styles.searchInput}
              placeholder="Buscar por assunto, palavra-chave..."
              value={search}
              onChange={(e) => { setSearch(e.target.value); setPage(0); }}
            />
          </div>
          <button className={styles.filterBtn}>
            ≡ Filtros
          </button>
          <button className={styles.filterBtn}>
            ≡ Ordenar
          </button>
        </div>

        {/* Card grid */}
        {loading && <p className={styles.empty}>Carregando...</p>}
        {!loading && questoes.length === 0 && (
          <p className={styles.empty}>Nenhuma questão encontrada.</p>
        )}

        <div className={styles.grid}>
          {questoes.map((q) => (
            <div
              key={q.id}
              className={`${styles.card} ${!q.ativo ? styles['card--inactive'] : ''}`}
              onClick={() => navigate(`/admin/perguntas/${q.id}/editar`)}
            >
              <div className={styles.cardTags}>
                <span className={styles.disciplinaTag}>{q.disciplina.replace('_', ' ')}</span>
                {!q.ativo && <span className={styles.inactiveTag}>INATIVA</span>}
              </div>
              <p className={styles.cardText}>{q.enunciado}</p>
              <div className={styles.cardFooter}>
                <span className={styles.cardInst}>{q.instituicao}</span>
                <span className={styles.cardId}>ID: {q.id}</span>
              </div>
              <button
                className={styles.deleteBtn}
                onClick={(e) => { e.stopPropagation(); setDeleteId(q.id); }}
                title="Excluir"
              >
                🗑
              </button>
            </div>
          ))}
        </div>

        {/* Pagination */}
        {total > 1 && (
          <div className={styles.pagination}>
            <button className={styles.pageBtn} disabled={page === 0} onClick={() => setPage(p => p - 1)}>‹</button>
            {Array.from({ length: total }, (_, i) => (
              <button
                key={i}
                className={`${styles.pageBtn} ${page === i ? styles['pageBtn--active'] : ''}`}
                onClick={() => setPage(i)}
              >
                {i + 1}
              </button>
            ))}
            <button className={styles.pageBtn} disabled={page >= total - 1} onClick={() => setPage(p => p + 1)}>›</button>
          </div>
        )}
      </div>

      {/* Delete modal */}
      {deleteId != null && (
        <div className={styles.overlay} onClick={() => setDeleteId(null)}>
          <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
            <div className={styles.modalIcon}>🗑</div>
            <h3 className={styles.modalTitle}>Tem certeza que deseja excluir esta questão?</h3>
            <p className={styles.modalDesc}>
              Esta ação não pode ser desfeita. A questão será removida permanentemente do banco de dados.
            </p>
            <div className={styles.modalActions}>
              <button className={styles.cancelBtn} onClick={() => setDeleteId(null)}>Cancelar</button>
              <button className={styles.confirmBtn} onClick={handleDelete}>Excluir</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
