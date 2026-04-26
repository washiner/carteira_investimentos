package com.washiner.carteira_investimentos.repository;

// Localização: src/main/java/com/washiner/carteira/repository/CategoriaRepository.java


import com.washiner.carteira_investimentos.enums.TipoCategoria;
import com.washiner.carteira_investimentos.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // Busca categorias por tipo — útil para filtrar por RENDA_FIXA, etc.
    List<Categoria> findByTipo(TipoCategoria tipo);

    // Busca por nome (case-sensitive)
    Optional<Categoria> findByNome(String nome);

    // Verifica se já existe categoria com esse nome (para evitar duplicatas)
    boolean existsByNome(String nome);
}