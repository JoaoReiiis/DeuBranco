ALTER TABLE partida
    ADD COLUMN iniciada_em TIMESTAMP,
    ADD COLUMN finalizada_em TIMESTAMP;

CREATE INDEX idx_partida_expiracao
    ON partida (status_sala, iniciada_em)
    WHERE status_sala = 'ANDAMENTO';
