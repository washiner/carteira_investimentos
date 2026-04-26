package com.washiner.carteira_investimentos.enums;

// Localização: src/main/java/com/washiner/carteira/enums/TipoAtivo.java

// Enum não precisa de imports especiais
// 'public' = acessível de qualquer lugar do projeto
public enum TipoAtivo {

    // Cada constante representa um tipo de investimento
    // O nome em MAIÚSCULO é convenção Java para constantes

    ACAO,          // Ação de empresa na bolsa (PETR4, VALE3)
    FII,           // Fundo de Investimento Imobiliário (MXRF11, HGLG11)
    ETF,           // Exchange Traded Fund (BOVA11, IVVB11)
    CDB,           // Certificado de Depósito Bancário
    TESOURO_DIRETO,// Títulos públicos do governo
    CRIPTO         // Criptomoedas (Bitcoin, Ethereum)

    // Quando você salva um Enum no banco com @Enumerated(EnumType.STRING),
    // o Hibernate salva o NOME da constante como texto: "ACAO", "FII", etc.
    // Isso é melhor que EnumType.ORDINAL (número) porque é legível no banco
}