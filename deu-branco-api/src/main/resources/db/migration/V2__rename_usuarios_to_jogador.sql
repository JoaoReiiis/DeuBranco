ALTER TABLE usuarios RENAME TO jogador;
ALTER TABLE jogador RENAME COLUMN senha_hash TO senha;
ALTER TABLE jogador RENAME CONSTRAINT uk_usuarios_email TO uk_jogador_email;
ALTER TABLE jogador ALTER COLUMN nome TYPE VARCHAR(100);
