package com.washiner.carteira_investimentos.controller;

// Localização: src/main/java/com/washiner/carteira/controller/CarteiraController.java

import com.washiner.carteira_investimentos.dto.request.CarteiraRequest;
import com.washiner.carteira_investimentos.dto.response.CarteiraResponse;
import com.washiner.carteira_investimentos.service.CarteiraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/carteiras")
@Tag(name = "Carteiras", description = "Gerenciamento de carteiras de investimento")
@RequiredArgsConstructor
public class CarteiraController {

    private final CarteiraService carteiraService;

    @Operation(summary = "Criar nova carteira para um usuário")
    @PostMapping
    public ResponseEntity<CarteiraResponse> criar(
            @RequestBody @Valid CarteiraRequest request) {

        CarteiraResponse response = carteiraService.criar(request);
        return ResponseEntity
                .created(URI.create("/carteiras/" + response.id()))
                .body(response);
    }

    @Operation(summary = "Buscar carteira por ID")
    @GetMapping("/{id}")
    public ResponseEntity<CarteiraResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(carteiraService.buscarPorId(id));
    }

    @Operation(summary = "Buscar carteira pelo ID do usuário")
    // GET /carteiras/usuario/5 — busca a carteira do usuário 5
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<CarteiraResponse> buscarPorUsuario(
            @PathVariable Long usuarioId) {

        return ResponseEntity.ok(carteiraService.buscarPorUsuario(usuarioId));
    }
}
