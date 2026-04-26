package com.washiner.carteira_investimentos.dto.response;

// Localização: src/main/java/com/washiner/carteira/dto/response/CategoriaResponse.java


import com.washiner.carteira_investimentos.enums.TipoCategoria;

public record CategoriaResponse(
        Long id,
        String nome,
        TipoCategoria tipo,
        String descricao
) {}
