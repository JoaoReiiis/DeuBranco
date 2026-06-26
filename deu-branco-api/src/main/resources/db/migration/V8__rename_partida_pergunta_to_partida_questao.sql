DO $$
BEGIN
    IF to_regclass('public.partida_pergunta') IS NOT NULL THEN
        ALTER TABLE partida_pergunta RENAME TO partida_questao;
        ALTER TABLE partida_questao RENAME COLUMN id_partida_pergunta TO id_partida_questao;
        ALTER TABLE partida_questao RENAME COLUMN id_pergunta TO id_questao;

        ALTER SEQUENCE partida_pergunta_id_partida_pergunta_seq
            RENAME TO partida_questao_id_partida_questao_seq;

        ALTER TABLE partida_questao RENAME CONSTRAINT partida_pergunta_pkey TO partida_questao_pkey;
        ALTER TABLE partida_questao RENAME CONSTRAINT fk_partida_pergunta_partida TO fk_partida_questao_partida;
        ALTER TABLE partida_questao RENAME CONSTRAINT fk_partida_pergunta_pergunta TO fk_partida_questao_questao;
        ALTER TABLE partida_questao RENAME CONSTRAINT uk_partida_pergunta_partida_pergunta TO uk_partida_questao_partida_questao;
        ALTER TABLE partida_questao RENAME CONSTRAINT uk_partida_pergunta_partida_ordem TO uk_partida_questao_partida_ordem;
        ALTER TABLE partida_questao RENAME CONSTRAINT ck_partida_pergunta_ordem TO ck_partida_questao_ordem;

        ALTER INDEX idx_partida_pergunta_partida RENAME TO idx_partida_questao_partida;
        ALTER INDEX idx_partida_pergunta_pergunta RENAME TO idx_partida_questao_questao;

        ALTER TABLE resposta RENAME COLUMN id_partida_pergunta TO id_partida_questao;

        ALTER TABLE resposta RENAME CONSTRAINT fk_resposta_partida_pergunta TO fk_resposta_partida_questao;
        ALTER TABLE resposta RENAME CONSTRAINT uk_resposta_participacao_partida_pergunta TO uk_resposta_participacao_partida_questao;

        ALTER INDEX idx_resposta_partida_pergunta RENAME TO idx_resposta_partida_questao;
    END IF;
END $$;
