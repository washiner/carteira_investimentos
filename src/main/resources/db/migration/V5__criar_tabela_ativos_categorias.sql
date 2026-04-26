-- Localização: src/main/resources/db/migration/V5__criar_tabela_ativos_categorias.sql
-- Depende de V3 (categorias) e V4 (ativos) — deve ser a última

-- Tabela intermediária do @ManyToMany entre Ativo e Categoria
-- Não tem PK própria — a PK é composta pelas duas FKs
CREATE TABLE ativos_categorias
(
    -- Referencia a tabela ativos
    ativo_id     BIGINT NOT NULL,

    -- Referencia a tabela categorias
    categoria_id BIGINT NOT NULL,

    -- PRIMARY KEY composta: a combinação ativo + categoria é única
    -- Um ativo não pode ter a mesma categoria duas vezes
    CONSTRAINT pk_ativos_categorias
        PRIMARY KEY (ativo_id, categoria_id),

    CONSTRAINT fk_ativos_categorias_ativo
        FOREIGN KEY (ativo_id)
            REFERENCES ativos (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_ativos_categorias_categoria
        FOREIGN KEY (categoria_id)
            REFERENCES categorias (id)
            ON DELETE CASCADE
);

COMMENT
ON TABLE ativos_categorias IS 'Tabela intermediária entre ativos e categorias';