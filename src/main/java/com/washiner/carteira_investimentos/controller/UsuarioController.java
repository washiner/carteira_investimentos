package com.washiner.carteira_investimentos.controller;

// Localização: src/main/java/com/washiner/carteira/controller/UsuarioController.java

import com.washiner.carteira_investimentos.dto.request.UsuarioRequest;
import com.washiner.carteira_investimentos.dto.response.UsuarioResponse;
import com.washiner.carteira_investimentos.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

// @RestController = @Controller + @ResponseBody
// Todos os métodos retornam JSON automaticamente
@RestController

// @RequestMapping: prefixo base de todas as rotas neste Controller
@RequestMapping("/usuarios")

// @Tag: agrupa os endpoints no Swagger com um nome e descrição
@Tag(name = "Usuários", description = "Gerenciamento de usuários do sistema")

@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // @Operation: descreve o endpoint no Swagger
    @Operation(summary = "Criar novo usuário")
    // @PostMapping: mapeia POST /usuarios
    @PostMapping
    public ResponseEntity<UsuarioResponse> criar(
            // @RequestBody: lê o JSON do corpo da requisição
            // @Valid: aciona as validações do Bean Validation (@NotBlank, @Email, etc.)
            @RequestBody @Valid UsuarioRequest request) {

        UsuarioResponse response = usuarioService.criar(request);

        // ResponseEntity.created(): retorna status 201 Created
        // URI.create(): cria a URL do recurso criado (boa prática REST)
        // Location header: indica onde o recurso pode ser acessado
        return ResponseEntity
                .created(URI.create("/usuarios/" + response.id()))
                .body(response);
    }

    @Operation(summary = "Listar todos os usuários com paginação")
    @GetMapping
    public ResponseEntity<Page<UsuarioResponse>> listar(
            // @PageableDefault: valores padrão quando não informados na requisição
            // size = 10: 10 itens por página
            // sort = "nome": ordena por nome por padrão
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {

        return ResponseEntity.ok(usuarioService.listar(pageable));
    }

    @Operation(summary = "Buscar usuário por ID")
    // @GetMapping("/{id}"): mapeia GET /usuarios/{id}
    // {id} é uma variável de caminho (path variable)
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(
            // @PathVariable: injeta o valor do {id} da URL
            @PathVariable Long id) {

        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @Operation(summary = "Atualizar usuário")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid UsuarioRequest request) {

        return ResponseEntity.ok(usuarioService.atualizar(id, request));
    }

    @Operation(summary = "Deletar usuário")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        // 204 No Content: operação bem-sucedida, sem corpo na resposta
        return ResponseEntity.noContent().build();
    }
}
