package com.washiner.carteira_investimentos.dto.request;

// Localização: src/main/java/com/washiner/carteira/dto/request/AtivoRequest.java

import com.washiner.carteira_investimentos.enums.TipoAtivo;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public record AtivoRequest(

        @NotBlank(message = "Ticker é obrigatório")
        @Size(max = 20)
        String ticker,

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 100)
        String nome,

        @NotNull(message = "Tipo é obrigatório")
        TipoAtivo tipo,

        // @DecimalMin: valor mínimo — a quantidade não pode ser negativa ou zero
        @NotNull(message = "Quantidade é obrigatória")
        @DecimalMin(value = "0.00000001", message = "Quantidade deve ser maior que zero")
        BigDecimal quantidade,

        @NotNull(message = "Preço médio é obrigatório")
        @DecimalMin(value = "0.01", message = "Preço médio deve ser maior que zero")
        BigDecimal precoMedio,

        // valorAtual é opcional — pode ser informado depois
        BigDecimal valorAtual,

        @NotNull(message = "ID da carteira é obrigatório")
        @Positive
        Long carteiraId,

        // Lista de IDs das categorias — pode ser vazia, mas não nula
        // @Size: no máximo 5 categorias por ativo (regra de negócio)
        @Size(max = 5, message = "Máximo de 5 categorias por ativo")
        List<Long> categoriaIds

) {}