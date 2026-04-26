package com.washiner.carteira_investimentos.dto.response;

// Localização: src/main/java/com/washiner/carteira/dto/response/UsuarioResponse.java


import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        // Note: senha NÃO aparece no Response — nunca exponha senhas na API
        LocalDateTime criadoEm
) {}
