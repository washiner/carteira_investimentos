-- Localização: src/main/resources/db/migration/V1__criar_tabela_usuarios.sql
-- O Flyway executa esse arquivo apenas uma vez, na primeira vez que vê ele

-- Cria a extensão para UUID (não vamos usar agora, mas é boa prática deixar)
-- CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE usuarios
(
    -- BIGSERIAL = inteiro grande que incrementa automaticamente
    -- É o equivalente ao @GeneratedValue(IDENTITY) do JPA
    id        BIGSERIAL PRIMARY KEY,

    -- VARCHAR(100): texto com até 100 caracteres
    -- NOT NULL: obrigatório
    -- UNIQUE: um email por usuário
    email     VARCHAR(100) NOT NULL UNIQUE,

    nome      VARCHAR(100) NOT NULL,

    -- Guarda o hash da senha (nunca texto puro)
    senha     VARCHAR(255) NOT NULL,

    -- TIMESTAMP: data e hora sem fuso horário
    -- DEFAULT NOW(): preenche automaticamente com o momento atual
    criado_em TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- Comentário na tabela — boa prática para documentação do banco
COMMENT
ON TABLE usuarios IS 'Tabela de usuários do sistema de carteira de investimentos';