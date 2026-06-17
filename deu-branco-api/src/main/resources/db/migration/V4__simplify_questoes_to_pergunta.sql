CREATE TABLE IF NOT EXISTS pergunta (
    id_pergunta BIGSERIAL PRIMARY KEY,
    enunciado TEXT NOT NULL,
    opcao_a TEXT NOT NULL,
    opcao_b TEXT NOT NULL,
    opcao_c TEXT NOT NULL,
    opcao_d TEXT NOT NULL,
    opcao_e TEXT NOT NULL,
    alternativa_correta CHAR(1) NOT NULL,
    disciplina VARCHAR(100) NOT NULL,
    instituicao VARCHAR(150) NOT NULL,
    imagens JSONB NOT NULL DEFAULT '[]'::jsonb,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT ck_pergunta_alternativa_correta
        CHECK (alternativa_correta IN ('A', 'B', 'C', 'D', 'E')),
    CONSTRAINT uk_pergunta_enunciado_instituicao UNIQUE (enunciado, instituicao)
);

DO $$
BEGIN
    IF to_regclass('public.questao') IS NOT NULL THEN
        EXECUTE $migration$
            INSERT INTO pergunta (
                id_pergunta,
                enunciado,
                opcao_a,
                opcao_b,
                opcao_c,
                opcao_d,
                opcao_e,
                alternativa_correta,
                disciplina,
                instituicao,
                imagens,
                ativo
            )
            SELECT
                q.id,
                COALESCE(NULLIF(TRIM(q.contexto), ''), q.titulo) AS enunciado,
                COALESCE(MAX(a.texto) FILTER (WHERE a.letra = 'A'), 'Alternativa A') AS opcao_a,
                COALESCE(MAX(a.texto) FILTER (WHERE a.letra = 'B'), 'Alternativa B') AS opcao_b,
                COALESCE(MAX(a.texto) FILTER (WHERE a.letra = 'C'), 'Alternativa C') AS opcao_c,
                COALESCE(MAX(a.texto) FILTER (WHERE a.letra = 'D'), 'Alternativa D') AS opcao_d,
                COALESCE(MAX(a.texto) FILTER (WHERE a.letra = 'E'), 'Alternativa E') AS opcao_e,
                q.alternativa_correta,
                q.area AS disciplina,
                'ENEM' AS instituicao,
                COALESCE(
                    jsonb_agg(DISTINCT qi.url) FILTER (WHERE qi.url IS NOT NULL),
                    '[]'::jsonb
                ) AS imagens,
                TRUE AS ativo
            FROM questao q
            LEFT JOIN alternativa a ON a.questao_id = q.id
            LEFT JOIN questao_imagem qi ON qi.questao_id = q.id
            GROUP BY q.id, q.contexto, q.titulo, q.alternativa_correta, q.area
            ON CONFLICT (enunciado, instituicao) DO NOTHING
        $migration$;
    END IF;
END $$;

SELECT setval(
    pg_get_serial_sequence('pergunta', 'id_pergunta'),
    COALESCE((SELECT MAX(id_pergunta) FROM pergunta), 1),
    (SELECT COUNT(*) > 0 FROM pergunta)
);

DROP TABLE IF EXISTS questao_imagem;
DROP TABLE IF EXISTS alternativa;
DROP TABLE IF EXISTS questao;

CREATE INDEX IF NOT EXISTS idx_pergunta_disciplina ON pergunta (disciplina);
CREATE INDEX IF NOT EXISTS idx_pergunta_instituicao ON pergunta (instituicao);
CREATE INDEX IF NOT EXISTS idx_pergunta_ativo ON pergunta (ativo);
