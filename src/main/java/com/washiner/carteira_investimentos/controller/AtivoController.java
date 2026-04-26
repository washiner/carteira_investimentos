package com.washiner.carteira_investimentos.controller;

// Localização: src/main/java/com/washiner/carteira/controller/AtivoController.java

import com.washiner.carteira_investimentos.dto.request.AtivoRequest;
import com.washiner.carteira_investimentos.dto.response.AtivoResponse;
import com.washiner.carteira_investimentos.service.AtivoService;
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

@RestController
@RequestMapping("/ativos")
@Tag(name = "Ativos", description = "Ativos de investimento das carteiras")
@RequiredArgsConstructor
public class AtivoController {

    private final AtivoService ativoService;

    @Operation(summary = "Adicionar ativo à carteira")
    @PostMapping
    public ResponseEntity<AtivoResponse> criar(
            @RequestBody @Valid AtivoRequest request) {

        AtivoResponse response = ativoService.criar(request);
        return ResponseEntity
                .created(URI.create("/ativos/" + response.id()))
                .body(response);
    }

    @Operation(summary = "Listar ativos de uma carteira com paginação")
    // GET /ativos/carteira/1?page=0&size=10
    @GetMapping("/carteira/{carteiraId}")
    public ResponseEntity<Page<AtivoResponse>> listarPorCarteira(
            @PathVariable Long carteiraId,
            @PageableDefault(size = 10, sort = "ticker") Pageable pageable) {

        return ResponseEntity.ok(ativoService.listarPorCarteira(carteiraId, pageable));
    }

    @Operation(summary = "Buscar ativo por ID")
    @GetMapping("/{id}")
    public ResponseEntity<AtivoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ativoService.buscarPorId(id));
    }

    @Operation(summary = "Atualizar ativo")
    @PutMapping("/{id}")
    public ResponseEntity<AtivoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid AtivoRequest request) {

        return ResponseEntity.ok(ativoService.atualizar(id, request));
    }

    @Operation(summary = "Remover ativo da carteira")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        ativoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}