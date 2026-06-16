CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    email VARCHAR(180) NOT NULL,
    senha_hash VARCHAR(60) NOT NULL,

    CONSTRAINT uk_usuarios_email UNIQUE (email)
);
