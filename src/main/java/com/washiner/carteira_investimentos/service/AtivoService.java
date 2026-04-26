package com.washiner.carteira_investimentos.service;

// Localização: src/main/java/com/washiner/carteira/service/AtivoService.java

import com.washiner.carteira_investimentos.dto.request.AtivoRequest;
import com.washiner.carteira_investimentos.dto.response.AtivoResponse;
import com.washiner.carteira_investimentos.dto.response.CategoriaResponse;
import com.washiner.carteira_investimentos.model.Ativo;
import com.washiner.carteira_investimentos.model.Carteira;
import com.washiner.carteira_investimentos.model.Categoria;
import com.washiner.carteira_investimentos.repository.AtivoRepository;
import com.washiner.carteira_investimentos.repository.CarteiraRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AtivoService {

    private final AtivoRepository ativoRepository;
    private final CarteiraRepository carteiraRepository;
    private final CategoriaService categoriaService; // Chama o Service, não o Repository direto

    @Transactional
    public AtivoResponse criar(AtivoRequest request) {

        // Verifica se o ticker já existe nessa carteira
        if (ativoRepository.existsByTickerAndCarteiraId(
                request.ticker(), request.carteiraId())) {
            throw new IllegalArgumentException(
                    "Já existe um ativo com o ticker '" + request.ticker() +
                            "' nesta carteira"
            );
        }

        // Busca a carteira
        Carteira carteira = carteiraRepository.findById(request.carteiraId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Carteira não encontrada com ID: " + request.carteiraId()
                ));

        // Busca as categorias pelos IDs fornecidos
        List<Categoria> categorias = new ArrayList<>();
        if (request.categoriaIds() != null && !request.categoriaIds().isEmpty()) {
            for (Long categoriaId : request.categoriaIds()) {
                // categoriaService.buscarEntidadePorId já lança exceção se não encontrar
                categorias.add(categoriaService.buscarEntidadePorId(categoriaId));
            }
        }

        Ativo ativo = Ativo.builder()
                .ticker(request.ticker().toUpperCase()) // Ticker sempre em maiúsculo
                .nome(request.nome())
                .tipo(request.tipo())
                .quantidade(request.quantidade())
                .precoMedio(request.precoMedio())
                .valorAtual(request.valorAtual())
                .carteira(carteira)
                .categorias(categorias)
                .build();

        Ativo salvo = ativoRepository.save(ativo);
        return toResponse(salvo);
    }

    @Transactional(readOnly = true)
    public Page<AtivoResponse> listarPorCarteira(Long carteiraId, Pageable pageable) {
        // Verifica se a carteira existe
        if (!carteiraRepository.existsById(carteiraId)) {
            throw new EntityNotFoundException(
                    "Carteira não encontrada com ID: " + carteiraId
            );
        }
        return ativoRepository.findByCarteiraId(carteiraId, pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public AtivoResponse buscarPorId(Long id) {
        Ativo ativo = ativoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Ativo não encontrado com ID: " + id
                ));
        return toResponse(ativo);
    }

    @Transactional
    public AtivoResponse atualizar(Long id, AtivoRequest request) {
        Ativo ativo = ativoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Ativo não encontrado com ID: " + id
                ));

        // Verifica ticker duplicado (ignora o próprio ativo)
        ativoRepository.findByTickerAndCarteiraId(request.ticker(), request.carteiraId())
                .ifPresent(existente -> {
                    if (!existente.getId().equals(id)) {
                        throw new IllegalArgumentException(
                                "Ticker '" + request.ticker() + "' já está em uso nesta carteira"
                        );
                    }
                });

        // Busca e atualiza categorias
        List<Categoria> novasCategorias = new ArrayList<>();
        if (request.categoriaIds() != null && !request.categoriaIds().isEmpty()) {
            for (Long categoriaId : request.categoriaIds()) {
                novasCategorias.add(categoriaService.buscarEntidadePorId(categoriaId));
            }
        }

        ativo.setTicker(request.ticker().toUpperCase());
        ativo.setNome(request.nome());
        ativo.setTipo(request.tipo());
        ativo.setQuantidade(request.quantidade());
        ativo.setPrecoMedio(request.precoMedio());
        ativo.setValorAtual(request.valorAtual());
        ativo.setCategorias(novasCategorias);
        // @PreUpdate no Model atualiza o atualizadoEm automaticamente

        return toResponse(ativoRepository.save(ativo));
    }

    @Transactional
    public void deletar(Long id) {
        if (!ativoRepository.existsById(id)) {
            throw new EntityNotFoundException("Ativo não encontrado com ID: " + id);
        }
        ativoRepository.deleteById(id);
    }

    private AtivoResponse toResponse(Ativo ativo) {
        // Calcula valor total investido: quantidade * precoMedio
        BigDecimal valorTotalInvestido = ativo.getQuantidade()
                .multiply(ativo.getPrecoMedio());

        // Converte a lista de Categoria → CategoriaResponse
        List<CategoriaResponse> categoriasResponse = ativo.getCategorias()
                .stream()
                .map(c -> new CategoriaResponse(
                        c.getId(), c.getNome(), c.getTipo(), c.getDescricao()
                ))
                .toList();

        return new AtivoResponse(
                ativo.getId(),
                ativo.getTicker(),
                ativo.getNome(),
                ativo.getTipo(),
                ativo.getQuantidade(),
                ativo.getPrecoMedio(),
                ativo.getValorAtual(),
                valorTotalInvestido,      // Campo calculado — não existe no banco
                ativo.getCarteira().getId(),
                categoriasResponse,
                ativo.getCriadoEm(),
                ativo.getAtualizadoEm()
        );
    }
}
