package com.washiner.carteira_investimentos.repository;

// Localização: src/main/java/com/washiner/carteira/repository/UsuarioRepository.java

// JpaRepository<T, ID>: T = tipo da entidade, ID = tipo da chave primária
import com.washiner.carteira_investimentos.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// @Repository: marca como componente Spring (embora JpaRepository já faça isso)
// Boa prática: deixar explícito para clareza do código
@Repository
// Herda: save, findById, findAll, deleteById, existsById, count, etc.
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Spring Data JPA gera o SQL automaticamente pelo nome do método
    // findBy + NomeDoCampo = SELECT * FROM usuarios WHERE email = ?
    Optional<Usuario> findByEmail(String email);

    // existsBy + NomeDoCampo = SELECT COUNT(*) > 0 FROM usuarios WHERE email = ?
    // Retorna boolean — mais eficiente que findByEmail quando só precisa saber se existe
    boolean existsByEmail(String email);
}