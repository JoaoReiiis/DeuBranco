import { useState, useEffect } from 'react';
import partidaService from '../../../services/partidaService';
import type { HistoricoPartidaResponse } from '../../../types/partida';
import styles from './HistoricoPartidas.module.scss';

const ITEMS_PER_PAGE = 9;

export function HistoricoPartidas() {
  const [historico, setHistorico] = useState<HistoricoPartidaResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [page, setPage] = useState(0);

  useEffect(() => {
    partidaService.getHistory().then(setHistorico).finally(() => setLoading(false));
  }, []);

  const filtered = historico.filter(h =>
    h.codigoPin.toLowerCase().includes(search.toLowerCase())
  );

  const totalPages = Math.ceil(filtered.length / ITEMS_PER_PAGE);
  const paged = filtered.slice(page * ITEMS_PER_PAGE, (page + 1) * ITEMS_PER_PAGE);

  function formatDate(dt: string) {
    return new Date(dt).toLocaleDateString('pt-BR', { day: '2-digit', month: 'short', year: 'numeric' });
  }

  return (
    <div className={styles.page}>
      <div className={styles.inner}>
        <div className={styles.header}>
          <h1 className={styles.title}>Histórico de Partidas</h1>
          <p className={styles.subtitle}>
            Revise suas partidas anteriores, analise seu desempenho e identifique áreas de melhoria.
          </p>
        </div>

        {/* Search + filters */}
        <div className={styles.controls}>
          <div className={styles.searchBox}>
            <span className={styles.searchIcon}>🔍</span>
            <input
              className={styles.searchInput}
              placeholder="Buscar partidas..."
              value={search}
              onChange={(e) => { setSearch(e.target.value); setPage(0); }}
            />
          </div>
          <div className={styles.filterPills}>
            <button
              className={`${styles.filterPill} ${search === '' ? styles['filterPill--active'] : ''}`}
              onClick={() => { setSearch(''); setPage(0); }}
            >
              Todas
            </button>
          </div>
        </div>

        {/* Cards grid */}
        {loading && <p className={styles.empty}>Carregando...</p>}

        {!loading && paged.length === 0 && (
          <p className={styles.empty}>Nenhuma partida encontrada.</p>
        )}

        <div className={styles.grid}>
          {paged.map((h) => (
            <div key={h.partidaId} className={styles.card}>
              <div className={styles.cardTop}>
                <span className={`${styles.statusBadge} ${styles[`statusBadge--${h.statusSala.toLowerCase()}`]}`}>
                  {h.statusSala}
                </span>
                <span className={styles.cardDate}>{formatDate(String(h.criadoEm))}</span>
                <div className={styles.rankBadge}>
                  <span className={styles.rankLabel}>PIN</span>
                  <span className={styles.rankNum}>{h.codigoPin}</span>
                </div>
              </div>

              <div className={styles.cardStats}>
                <div className={styles.stat}>
                  <span className={styles.statLabel}>Pontos</span>
                  <span className={styles.statValue}>{h.scorePartida}</span>
                </div>
              </div>
            </div>
          ))}
        </div>

        {/* Pagination */}
        {totalPages > 1 && (
          <div className={styles.pagination}>
            <button
              className={styles.pageBtn}
              disabled={page === 0}
              onClick={() => setPage(p => p - 1)}
            >
              ‹
            </button>
            {Array.from({ length: totalPages }, (_, i) => (
              <button
                key={i}
                className={`${styles.pageBtn} ${page === i ? styles['pageBtn--active'] : ''}`}
                onClick={() => setPage(i)}
              >
                {i + 1}
              </button>
            ))}
            <button
              className={styles.pageBtn}
              disabled={page >= totalPages - 1}
              onClick={() => setPage(p => p + 1)}
            >
              ›
            </button>
          </div>
        )}
      </div>
    </div>
  );
}
