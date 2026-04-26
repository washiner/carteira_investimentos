package com.washiner.carteira_investimentos.repository;

// Localização: src/main/java/com/washiner/carteira/repository/CarteiraRepository.java


import com.washiner.carteira_investimentos.model.Carteira;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarteiraRepository extends JpaRepository<Carteira, Long> {

    // Busca a carteira pelo ID do usuário
    // Spring traduz: SELECT * FROM carteiras WHERE usuario_id = ?
    Optional<Carteira> findByUsuarioId(Long usuarioId);

    // Verifica se já existe carteira para esse usuário (evita duplicata no @OneToOne)
    boolean existsByUsuarioId(Long usuarioId);
}