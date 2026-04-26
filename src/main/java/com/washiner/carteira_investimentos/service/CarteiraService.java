package com.washiner.carteira_investimentos.service;

// Localização: src/main/java/com/washiner/carteira/service/CarteiraService.java

import com.washiner.carteira_investimentos.dto.request.CarteiraRequest;
import com.washiner.carteira_investimentos.dto.response.CarteiraResponse;
import com.washiner.carteira_investimentos.model.Carteira;
import com.washiner.carteira_investimentos.model.Usuario;
import com.washiner.carteira_investimentos.repository.CarteiraRepository;
import com.washiner.carteira_investimentos.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CarteiraService {

    private final CarteiraRepository carteiraRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public CarteiraResponse criar(CarteiraRequest request) {

        // Regra @OneToOne: um usuário só pode ter uma carteira
        if (carteiraRepository.existsByUsuarioId(request.usuarioId())) {
            throw new IllegalArgumentException(
                    "Usuário já possui uma carteira cadastrada"
            );
        }

        // Verifica se o usuário existe
        Usuario usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Usuário não encontrado com ID: " + request.usuarioId()
                ));

        Carteira carteira = Carteira.builder()
                .nome(request.nome())
                .usuario(usuario)  // Seta o relacionamento
                .build();
        // valorTotal é inicializado como ZERO no @PrePersist

        Carteira salva = carteiraRepository.save(carteira);
        return toResponse(salva);
    }

    @Transactional(readOnly = true)
    public CarteiraResponse buscarPorUsuario(Long usuarioId) {
        Carteira carteira = carteiraRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Carteira não encontrada para o usuário com ID: " + usuarioId
                ));
        return toResponse(carteira);
    }

    @Transactional(readOnly = true)
    public CarteiraResponse buscarPorId(Long id) {
        Carteira carteira = carteiraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Carteira não encontrada com ID: " + id
                ));
        return toResponse(carteira);
    }

    private CarteiraResponse toResponse(Carteira carteira) {
        return new CarteiraResponse(
                carteira.getId(),
                carteira.getNome(),
                carteira.getValorTotal(),
                carteira.getUsuario().getId(),
                carteira.getUsuario().getNome(), // Campo extra útil no Response
                carteira.getCriadoEm()
        );
    }
}
