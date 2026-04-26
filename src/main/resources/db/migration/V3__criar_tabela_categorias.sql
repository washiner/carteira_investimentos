-- Localização: src/main/resources/db/migration/V3__criar_tabela_categorias.sql
-- Categorias não dependem de outras tabelas — pode vir antes dos ativos

CREATE TABLE categorias
(
    id        BIGSERIAL PRIMARY KEY,
    nome      VARCHAR(100) NOT NULL,

    -- Salva o nome do Enum como texto (EnumType.STRING no JPA)
    -- CHECK: garante que só valores válidos entrem no banco
    tipo      VARCHAR(50)  NOT NULL
        CHECK (tipo IN (
                        'RENDA_VARIAVEL',
                        'RENDA_FIXA',
                        'FUNDO_IMOBILIARIO',
                        'FUNDO_INTERNACIONAL',
                        'RESERVA_EMERGENCIA'
            )),

    descricao VARCHAR(255),

    CONSTRAINT uq_categorias_nome UNIQUE (nome)
);

COMMENT
ON TABLE categorias IS 'Categorias de classificação dos ativos';

-- Insere categorias padrão para o sistema já ter dados iniciais
INSERT INTO categorias (nome, tipo, descricao)
VALUES ('Renda Variável', 'RENDA_VARIAVEL', 'Ações e ativos com retorno imprevisível'),
       ('Renda Fixa', 'RENDA_FIXA', 'CDB, Tesouro Direto e ativos com retorno previsível'),
       ('Fundos Imobiliários', 'FUNDO_IMOBILIARIO', 'FIIs negociados na bolsa'),
       ('Fundos Internacionais', 'FUNDO_INTERNACIONAL', 'Ativos em moeda estrangeira'),
       ('Reserva de Emergência', 'RESERVA_EMERGENCIA', 'Ativos de alta liquidez para emergências');