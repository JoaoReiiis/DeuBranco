CREATE TABLE questao (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(160) NOT NULL,
    indice INTEGER NOT NULL,
    area VARCHAR(80) NOT NULL,
    ano INTEGER NOT NULL,
    contexto TEXT,
    alternativas TEXT,
    alternativa_correta CHAR(1) NOT NULL,

    CONSTRAINT uk_questao_ano_indice_area UNIQUE (ano, indice, area)
);

CREATE INDEX idx_questao_ano ON questao (ano);
CREATE INDEX idx_questao_ano_area ON questao (ano, area);

CREATE TABLE alternativa (
    id BIGSERIAL PRIMARY KEY,
    questao_id BIGINT NOT NULL,
    letra CHAR(1) NOT NULL,
    texto TEXT NOT NULL,
    ordem INTEGER NOT NULL,

    CONSTRAINT fk_alternativa_questao
        FOREIGN KEY (questao_id) REFERENCES questao(id) ON DELETE CASCADE,
    CONSTRAINT uk_alternativa_questao_letra UNIQUE (questao_id, letra),
    CONSTRAINT uk_alternativa_questao_ordem UNIQUE (questao_id, ordem)
);

CREATE TABLE questao_imagem (
    id BIGSERIAL PRIMARY KEY,
    questao_id BIGINT NOT NULL,
    url TEXT NOT NULL,
    ordem INTEGER NOT NULL,

    CONSTRAINT fk_questao_imagem_questao
        FOREIGN KEY (questao_id) REFERENCES questao(id) ON DELETE CASCADE,
    CONSTRAINT uk_questao_imagem_ordem UNIQUE (questao_id, ordem)
);
