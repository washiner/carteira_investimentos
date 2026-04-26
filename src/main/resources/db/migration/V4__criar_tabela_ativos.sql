-- Localização: src/main/resources/db/migration/V4__criar_tabela_ativos.sql
-- Depende de V2 (carteiras)

CREATE TABLE ativos
(
    id            BIGSERIAL PRIMARY KEY,
    ticker        VARCHAR(20)    NOT NULL,
    nome          VARCHAR(100)   NOT NULL,

    -- Tipo do ativo — salvo como texto do Enum
    tipo          VARCHAR(30)    NOT NULL
        CHECK (tipo IN (
                        'ACAO', 'FII', 'ETF', 'CDB',
                        'TESOURO_DIRETO', 'CRIPTO'
            )),

    -- NUMERIC(19,8): 8 casas decimais para suportar frações de cripto
    quantidade    NUMERIC(19, 8) NOT NULL DEFAULT 0,
    preco_medio   NUMERIC(19, 4) NOT NULL,
    valor_atual   NUMERIC(19, 4),

    criado_em     TIMESTAMP      NOT NULL DEFAULT NOW(),
    atualizado_em TIMESTAMP      NOT NULL DEFAULT NOW(),

    -- FK para carteiras — um ativo pertence a uma carteira
    carteira_id   BIGINT         NOT NULL,

    CONSTRAINT fk_ativos_carteiras
        FOREIGN KEY (carteira_id)
            REFERENCES carteiras (id)
            ON DELETE CASCADE
);

-- Índice na FK: acelera buscas do tipo "todos os ativos desta carteira"
-- Sem índice, o banco faz uma varredura completa (lento com muitos dados)
CREATE INDEX idx_ativos_carteira_id ON ativos (carteira_id);

-- Índice no ticker: buscas por ticker serão rápidas
CREATE INDEX idx_ativos_ticker ON ativos (ticker);

COMMENT
ON TABLE ativos IS 'Ativos de investimento das carteiras';