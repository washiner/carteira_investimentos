package com.washiner.carteira_investimentos.dto.request;

// Localização: src/main/java/com/washiner/carteira/dto/request/UsuarioRequest.java


// Imports de validação — Bean Validation (Jakarta Validation)
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Record: define um DTO imutável
// O compilador gera: construtor, getters (sem o "get"), equals, hashCode, toString
public record UsuarioRequest(

        // @NotBlank: não aceita null, vazio "" ou só espaços "   "
        // message: mensagem de erro personalizada
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
        String nome,

        // @Email: valida o formato do email
        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, max = 100, message = "Senha deve ter entre 6 e 100 caracteres")
        String senha

) {}
// O Record em Java usa {} vazio quando não tem métodos extras