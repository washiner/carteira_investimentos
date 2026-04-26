package com.washiner.carteira_investimentos.exception;

// Localização: src/main/java/com/washiner/carteira/exception/GlobalExceptionHandler.java

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

// @RestControllerAdvice: intercepta exceções lançadas em qualquer Controller
// É um @ControllerAdvice + @ResponseBody combinados
// Funciona como um "guarda-chuva" de erros para toda a aplicação
@RestControllerAdvice
public class GlobalExceptionHandler {

    // @ExceptionHandler: diz ao Spring qual tipo de exceção este método trata
    // Quando qualquer lugar lançar EntityNotFoundException, este método é chamado
    @ExceptionHandler(jakarta.persistence.EntityNotFoundException.class)
    public ResponseEntity<ErroResponse> handleEntityNotFound(
            jakarta.persistence.EntityNotFoundException ex) {

        ErroResponse erro = new ErroResponse(
                HttpStatus.NOT_FOUND.value(),  // 404
                "Não Encontrado",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    // Trata erros de validação (@Valid no Controller)
    // MethodArgumentNotValidException: lançada quando @NotBlank, @Email, etc. falham
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        // Coleta TODOS os campos que falharam na validação
        List<String> detalhes = ex.getBindingResult()
                .getFieldErrors()                               // Pega todos os FieldErrors
                .stream()
                .map(FieldError::getDefaultMessage)             // Pega a mensagem de cada um
                .toList();                                      // Converte para List

        ErroResponse erro = new ErroResponse(
                HttpStatus.BAD_REQUEST.value(),  // 400
                "Dados Inválidos",
                "Verifique os campos informados",
                java.time.LocalDateTime.now(),
                detalhes
        );

        return ResponseEntity.badRequest().body(erro);
    }

    // Trata violações de regras de negócio (ex: email já cadastrado)
    // IllegalArgumentException: convenção para "dados inválidos pela regra de negócio"
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponse> handleIllegalArgument(
            IllegalArgumentException ex) {

        ErroResponse erro = new ErroResponse(
                HttpStatus.CONFLICT.value(),  // 409 Conflict
                "Conflito",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    // Captura QUALQUER outra exceção não tratada — "net de segurança"
    // Sem esse handler, o Spring retornaria uma página HTML de erro (feio e inseguro)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> handleGenericException(Exception ex) {

        ErroResponse erro = new ErroResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),  // 500
                "Erro Interno",
                "Ocorreu um erro inesperado. Tente novamente."
                // Nunca exponha ex.getMessage() aqui em produção
                // Pode vazar informações sensíveis do servidor
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }
}
