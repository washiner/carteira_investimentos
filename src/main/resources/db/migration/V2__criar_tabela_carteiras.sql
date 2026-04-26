-- Localização: src/main/resources/db/migration/V2__criar_tabela_carteiras.sql
-- Esta migration DEPENDE de V1 — usuarios já deve existir

CREATE TABLE carteiras
(
    id          BIGSERIAL PRIMARY KEY,
    nome        VARCHAR(100)   NOT NULL,

    -- NUMERIC(19,4): número com até 19 dígitos, sendo 4 decimais
    -- DEFAULT 0: começa em zero
    valor_total NUMERIC(19, 4) NOT NULL DEFAULT 0,

    criado_em   TIMESTAMP      NOT NULL DEFAULT NOW(),

    -- FOREIGN KEY: referencia a tabela usuarios
    -- UNIQUE: garante que um usuário tem no máximo uma carteira (@OneToOne)
    usuario_id  BIGINT         NOT NULL UNIQUE,

    -- CONSTRAINT nomeia a FK — facilita mensagens de erro e manutenção
    -- REFERENCES: aponta para qual tabela/coluna
    -- ON DELETE CASCADE: se o usuário for deletado, deleta a carteira junto
    CONSTRAINT fk_carteiras_usuarios
        FOREIGN KEY (usuario_id)
            REFERENCES usuarios (id)
            ON DELETE CASCADE
);

COMMENT
ON TABLE carteiras IS 'Carteiras de investimento dos usuários';