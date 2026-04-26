package com.washiner.carteira_investimentos.dto.response;

// Localização: src/main/java/com/washiner/carteira/dto/response/AtivoResponse.java

import com.washiner.carteira_investimentos.enums.TipoAtivo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record AtivoResponse(
        Long id,
        String ticker,
        String nome,
        TipoAtivo tipo,
        BigDecimal quantidade,
        BigDecimal precoMedio,
        BigDecimal valorAtual,

        // Valor total do ativo: quantidade * precoMedio (campo calculado)
        // Não existe no banco — calculamos no Service antes de retornar
        BigDecimal valorTotalInvestido,

        Long carteiraId,

        // Lista de categorias — já como Response (não como IDs)
        List<CategoriaResponse> categorias,

        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {}
