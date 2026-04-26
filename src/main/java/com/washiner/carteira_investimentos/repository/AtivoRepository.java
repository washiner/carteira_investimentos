package com.washiner.carteira_investimentos.repository;

// Localização: src/main/java/com/washiner/carteira/repository/AtivoRepository.java


import com.washiner.carteira_investimentos.enums.TipoAtivo;
import com.washiner.carteira_investimentos.model.Ativo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AtivoRepository extends JpaRepository<Ativo, Long> {

    // Busca todos os ativos de uma carteira — com paginação
    // Page<Ativo>: resultado paginado (você já conhece de Fase 1)
    // Pageable: parâmetros de paginação (página, tamanho, ordenação)
    Page<Ativo> findByCarteiraId(Long carteiraId, Pageable pageable);

    // Busca por ticker dentro de uma carteira específica
    Optional<Ativo> findByTickerAndCarteiraId(String ticker, Long carteiraId);

    // Busca ativos por tipo dentro de uma carteira
    List<Ativo> findByTipoAndCarteiraId(TipoAtivo tipo, Long carteiraId);

    // Verifica se o ticker já existe nessa carteira
    boolean existsByTickerAndCarteiraId(String ticker, Long carteiraId);
}
