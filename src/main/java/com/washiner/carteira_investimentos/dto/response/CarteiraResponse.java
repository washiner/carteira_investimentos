package com.washiner.carteira_investimentos.dto.response;

// Localização: src/main/java/com/washiner/carteira/dto/response/CarteiraResponse.java


import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CarteiraResponse(
        Long id,
        String nome,
        BigDecimal valorTotal,
        Long usuarioId,       // Só o ID do usuário — evita recursão infinita
        String nomeUsuario,   // Campo extra: útil na interface sem precisar de outra chamada
        LocalDateTime criadoEm
) {}
