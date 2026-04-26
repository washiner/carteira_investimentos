package com.washiner.carteira_investimentos.dto.request;

// Localização: src/main/java/com/washiner/carteira/dto/request/CategoriaRequest.java

import com.washiner.carteira_investimentos.enums.TipoCategoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoriaRequest(

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 100)
        String nome,

        // @NotNull valida que o enum foi enviado
        // O Spring converte automaticamente a String "RENDA_FIXA" para o enum TipoCategoria.RENDA_FIXA
        @NotNull(message = "Tipo é obrigatório")
        TipoCategoria tipo,

        @Size(max = 255)
        String descricao

) {}
