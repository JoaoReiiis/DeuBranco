import { useState, type FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import questaoService from '../../../services/questaoService';
import type { QuestaoResponse } from '../../../types/questao';
import { DISCIPLINAS, INSTITUICOES } from '../../../types/questao';
import { Card } from '../../../components/ui/Card/Card';
import { Select } from '../../../components/ui/Select/Select';
import { Button } from '../../../components/ui/Button/Button';
import styles from '../GerirPerguntas/GerirPerguntas.module.scss';

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
      <div className={styles.header}>
        <h1 className={styles.title}>Buscar perguntas</h1>
        <Button variant="secondary" onClick={() => navigate('/admin/perguntas')}>Voltar</Button>
      </div>
      <Card>
        <form onSubmit={handleSearch} style={{ display: 'flex', gap: '12px', flexWrap: 'wrap', alignItems: 'flex-end' }}>
          <Select
            label="Disciplina"
            options={[{ value: '', label: 'Todas' }, ...DISCIPLINAS]}
            value={disciplina}
            onChange={(e) => setDisciplina(e.target.value)}
          />
          <Select
            label="Instituição"
            options={[{ value: '', label: 'Todas' }, ...INSTITUICOES]}
            value={instituicao}
            onChange={(e) => setInstituicao(e.target.value)}
          />
          <Button type="submit" disabled={loading}>
            {loading ? 'Buscando...' : 'Pesquisar'}
          </Button>
        </form>
      </Card>

      {results !== null && (
        <Card>
          {results.length === 0 ? (
            <p className={styles.empty}>Nenhuma pergunta encontrada para os critérios informados.</p>
          ) : (
            <div className={styles.list}>
              {results.map((q) => (
                <div key={q.id} className={styles.questionRow}>
                  <div className={styles.questionInfo}>
                    <p className={styles.questionText}>{q.enunciado}</p>
                    <div className={styles.questionMeta}>
                      <span>{q.disciplina.replace('_', ' ')}</span>
                      <span>·</span>
                      <span>{q.instituicao}</span>
                      <span>· Gabarito: <strong>{q.alternativaCorreta}</strong></span>
                    </div>
                  </div>
                  <Button size="sm" variant="secondary" onClick={() => navigate(`/admin/perguntas/${q.id}/editar`)}>
                    Editar
                  </Button>
                </div>
              ))}
            </div>
          )}
        </Card>
      )}
    </div>
  );
}
