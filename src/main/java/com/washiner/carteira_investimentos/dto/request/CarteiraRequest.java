package com.washiner.carteira_investimentos.dto.request;

// Localização: src/main/java/com/washiner/carteira/dto/request/CarteiraRequest.java


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CarteiraRequest(

        @NotBlank(message = "Nome da carteira é obrigatório")
        @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
        String nome,

        // @NotNull: o ID do usuário não pode ser nulo
        // @Positive: deve ser maior que zero
        @NotNull(message = "ID do usuário é obrigatório")
        @Positive(message = "ID do usuário deve ser positivo")
        Long usuarioId

) {}
