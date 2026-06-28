import { useState, type FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import questaoService from '../../../services/questaoService';
import type { QuestaoResponse } from '../../../types/questao';
import { DISCIPLINAS, INSTITUICOES } from '../../../types/questao';
import { Button } from '../../../components/ui/Button/Button';
import styles from './VisualizarPergunta.module.scss';

export function VisualizarPergunta() {
  const navigate = useNavigate();
  const [disciplina, setDisciplina] = useState('');
  const [instituicao, setInstituicao] = useState('');
  const [results, setResults] = useState<QuestaoResponse[] | null>(null);
  const [loading, setLoading] = useState(false);

  async function handleSearch(e: FormEvent) {
    e.preventDefault();
    setLoading(true);
    try {
      const params: Parameters<typeof questaoService.list>[0] = { size: 50 };
      if (disciplina) params.disciplina = disciplina as QuestaoResponse['disciplina'];
      if (instituicao) params.instituicao = instituicao as QuestaoResponse['instituicao'];
      const res = await questaoService.list(params);
      setResults(res.content);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className={styles.page}>
      <div className={styles.inner}>
        <div className={styles.header}>
          <h1 className={styles.title}>Buscar Questões</h1>
          <Button variant="secondary" onClick={() => navigate('/admin/perguntas')}>Voltar</Button>
        </div>

        <form className={styles.searchForm} onSubmit={handleSearch}>
          <div className={styles.field}>
            <label className={styles.label}>Disciplina</label>
            <select className={styles.select} value={disciplina} onChange={(e) => setDisciplina(e.target.value)}>
              <option value="">Todas</option>
              {DISCIPLINAS.map(d => <option key={d.value} value={d.value}>{d.label}</option>)}
            </select>
          </div>
          <div className={styles.field}>
            <label className={styles.label}>Instituição</label>
            <select className={styles.select} value={instituicao} onChange={(e) => setInstituicao(e.target.value)}>
              <option value="">Todas</option>
              {INSTITUICOES.map(i => <option key={i.value} value={i.value}>{i.label}</option>)}
            </select>
          </div>
          <Button type="submit" disabled={loading}>
            {loading ? 'Buscando...' : 'Pesquisar'}
          </Button>
        </form>

        {results !== null && (
          <div className={styles.grid}>
            {results.length === 0 && (
              <p className={styles.empty}>Nenhuma questão encontrada.</p>
            )}
            {results.map((q) => (
              <div
                key={q.id}
                className={styles.card}
                onClick={() => navigate(`/admin/perguntas/${q.id}/editar`)}
              >
                <div className={styles.cardTags}>
                  <span className={styles.tag}>{q.disciplina.replace('_', ' ')}</span>
                  <span className={styles.idText}>ID: {q.id}</span>
                </div>
                <p className={styles.cardText}>{q.enunciado}</p>
                <div className={styles.cardMeta}>
                  <span>{q.instituicao}</span>
                  <span>Gabarito: <strong>{q.alternativaCorreta}</strong></span>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
