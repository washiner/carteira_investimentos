package com.washiner.carteira_investimentos.controller;

// Localização: src/main/java/com/washiner/carteira/controller/CategoriaController.java

import com.washiner.carteira_investimentos.dto.request.CategoriaRequest;
import com.washiner.carteira_investimentos.dto.response.CategoriaResponse;
import com.washiner.carteira_investimentos.enums.TipoCategoria;
import com.washiner.carteira_investimentos.service.CategoriaService;
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
import java.util.List;

@RestController
@RequestMapping("/categorias")
@Tag(name = "Categorias", description = "Categorias de classificação dos ativos")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @Operation(summary = "Criar nova categoria")
    @PostMapping
    public ResponseEntity<CategoriaResponse> criar(
            @RequestBody @Valid CategoriaRequest request) {

        CategoriaResponse response = categoriaService.criar(request);
        return ResponseEntity
                .created(URI.create("/categorias/" + response.id()))
                .body(response);
    }

    @Operation(summary = "Listar todas as categorias com paginação")
    @GetMapping
    public ResponseEntity<Page<CategoriaResponse>> listar(
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {

        return ResponseEntity.ok(categoriaService.listar(pageable));
    }

    @Operation(summary = "Listar categorias por tipo")
    // GET /categorias/tipo/RENDA_FIXA
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<CategoriaResponse>> listarPorTipo(
            @PathVariable TipoCategoria tipo) {

        return ResponseEntity.ok(categoriaService.listarPorTipo(tipo));
    }

    @Operation(summary = "Buscar categoria por ID")
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.buscarPorId(id));
    }
}
