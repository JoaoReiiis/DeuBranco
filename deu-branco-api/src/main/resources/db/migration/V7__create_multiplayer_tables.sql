CREATE TABLE partida (
    id_partida BIGSERIAL PRIMARY KEY,
    id_jogador_host BIGINT NOT NULL,
    tempo_de_jogo INTEGER NOT NULL,
    codigo_pin VARCHAR(10) NOT NULL,
    status_sala VARCHAR(30) NOT NULL DEFAULT 'AGUARDANDO',
    criado_em TIMESTAMP NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_partida_jogador_host
        FOREIGN KEY (id_jogador_host) REFERENCES jogador(id) ON DELETE CASCADE,
    CONSTRAINT ck_partida_tempo_de_jogo CHECK (tempo_de_jogo > 0),
    CONSTRAINT ck_partida_status_sala
        CHECK (status_sala IN ('AGUARDANDO', 'ANDAMENTO', 'FINALIZADA', 'CANCELADA'))
);

CREATE INDEX idx_partida_status_sala ON partida (status_sala);
CREATE INDEX idx_partida_jogador_host ON partida (id_jogador_host);
CREATE UNIQUE INDEX uk_partida_codigo_pin_nao_finalizada
    ON partida (codigo_pin)
    WHERE status_sala <> 'FINALIZADA';

CREATE TABLE partida_questao (
    id_partida_questao BIGSERIAL PRIMARY KEY,
    id_partida BIGINT NOT NULL,
    id_questao BIGINT NOT NULL,
    ordem INTEGER NOT NULL,

    CONSTRAINT fk_partida_questao_partida
        FOREIGN KEY (id_partida) REFERENCES partida(id_partida) ON DELETE CASCADE,
    CONSTRAINT fk_partida_questao_questao
        FOREIGN KEY (id_questao) REFERENCES pergunta(id_pergunta),
    CONSTRAINT uk_partida_questao_partida_questao UNIQUE (id_partida, id_questao),
    CONSTRAINT uk_partida_questao_partida_ordem UNIQUE (id_partida, ordem),
    CONSTRAINT ck_partida_questao_ordem CHECK (ordem > 0)
);

CREATE INDEX idx_partida_questao_partida ON partida_questao (id_partida);
CREATE INDEX idx_partida_questao_questao ON partida_questao (id_questao);

CREATE TABLE participacao (
    id_participacao BIGSERIAL PRIMARY KEY,
    id_partida BIGINT NOT NULL,
    id_jogador BIGINT NOT NULL,
    score_partida INTEGER NOT NULL DEFAULT 0,

    CONSTRAINT fk_participacao_partida
        FOREIGN KEY (id_partida) REFERENCES partida(id_partida) ON DELETE CASCADE,
    CONSTRAINT fk_participacao_jogador
        FOREIGN KEY (id_jogador) REFERENCES jogador(id) ON DELETE CASCADE,
    CONSTRAINT uk_participacao_partida_jogador UNIQUE (id_partida, id_jogador),
    CONSTRAINT ck_participacao_score_partida CHECK (score_partida >= 0)
);

CREATE INDEX idx_participacao_partida ON participacao (id_partida);
CREATE INDEX idx_participacao_jogador ON participacao (id_jogador);

CREATE TABLE resposta (
    id_resposta BIGSERIAL PRIMARY KEY,
    id_participacao BIGINT NOT NULL,
    id_partida_questao BIGINT NOT NULL,
    alternativa_marcada CHAR(1),
    tempo_resposta INTEGER NOT NULL,
    acertou BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_resposta_participacao
        FOREIGN KEY (id_participacao) REFERENCES participacao(id_participacao) ON DELETE CASCADE,
    CONSTRAINT fk_resposta_partida_questao
        FOREIGN KEY (id_partida_questao) REFERENCES partida_questao(id_partida_questao) ON DELETE CASCADE,
    CONSTRAINT uk_resposta_participacao_partida_questao UNIQUE (id_participacao, id_partida_questao),
    CONSTRAINT ck_resposta_alternativa_marcada
        CHECK (alternativa_marcada IS NULL OR alternativa_marcada IN ('A', 'B', 'C', 'D', 'E')),
    CONSTRAINT ck_resposta_tempo_resposta CHECK (tempo_resposta >= 0)
);

CREATE INDEX idx_resposta_participacao ON resposta (id_participacao);
CREATE INDEX idx_resposta_partida_questao ON resposta (id_partida_questao);
