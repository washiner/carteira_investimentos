package com.washiner.carteira_investimentos.exception;

// Localização: src/main/java/com/washiner/carteira/exception/ErroResponse.java

import java.time.LocalDateTime;
import java.util.List;

// Record para padronizar a resposta de erro da API
// Toda vez que algo der errado, o cliente recebe esse mesmo formato
public record ErroResponse(
        int status,               // Código HTTP (400, 404, 409, 500...)
        String erro,              // Nome do erro ("Não Encontrado", "Conflito")
        String mensagem,          // Descrição legível do problema
        LocalDateTime timestamp,  // Quando aconteceu
        List<String> detalhes     // Lista de erros de validação (pode ser null)
) {
    // Construtor de conveniência — para erros sem detalhes de validação
    public ErroResponse(int status, String erro, String mensagem) {
        this(status, erro, mensagem, LocalDateTime.now(), null);
    }
}