package com.washiner.carteira_investimentos.service;

// Localização: src/main/java/com/washiner/carteira/service/CategoriaService.java

import com.washiner.carteira_investimentos.dto.request.CategoriaRequest;
import com.washiner.carteira_investimentos.dto.response.CategoriaResponse;
import com.washiner.carteira_investimentos.enums.TipoCategoria;
import com.washiner.carteira_investimentos.model.Categoria;
import com.washiner.carteira_investimentos.repository.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Transactional
    public CategoriaResponse criar(CategoriaRequest request) {
        if (categoriaRepository.existsByNome(request.nome())) {
            throw new IllegalArgumentException(
                    "Já existe uma categoria com o nome: " + request.nome()
            );
        }

        Categoria categoria = Categoria.builder()
                .nome(request.nome())
                .tipo(request.tipo())
                .descricao(request.descricao())
                .build();

        return toResponse(categoriaRepository.save(categoria));
    }

    @Transactional(readOnly = true)
    public Page<CategoriaResponse> listar(Pageable pageable) {
        return categoriaRepository.findAll(pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponse> listarPorTipo(TipoCategoria tipo) {
        return categoriaRepository.findByTipo(tipo)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaResponse buscarPorId(Long id) {
        return toResponse(categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Categoria não encontrada com ID: " + id
                )));
    }

    // Método de uso interno — outros Services chamam esse método
    // É public porque AtivoService vai precisar buscar categorias por ID
    @Transactional(readOnly = true)
    public Categoria buscarEntidadePorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Categoria não encontrada com ID: " + id
                ));
    }

    private CategoriaResponse toResponse(Categoria categoria) {
        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNome(),
                categoria.getTipo(),
                categoria.getDescricao()
        );
    }
}
