package com.washiner.carteira_investimentos.service;

// Localização: src/main/java/com/washiner/carteira/service/UsuarioService.java

import com.washiner.carteira_investimentos.dto.request.UsuarioRequest;
import com.washiner.carteira_investimentos.dto.response.UsuarioResponse;
import com.washiner.carteira_investimentos.model.Usuario;
import com.washiner.carteira_investimentos.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// @Service: marca como componente de serviço — o Spring gerencia o ciclo de vida
// @RequiredArgsConstructor (Lombok): gera construtor com os campos 'final'
//   Isso é injeção de dependência por construtor — mais recomendado que @Autowired
@Service
@RequiredArgsConstructor
public class UsuarioService {

    // 'final': campo obrigatório — o Lombok injeta via construtor
    private final UsuarioRepository usuarioRepository;

    // @Transactional: operação de escrita — garante rollback se falhar
    @Transactional
    public UsuarioResponse criar(UsuarioRequest request) {

        // Regra de negócio: email único
        if (usuarioRepository.existsByEmail(request.email())) {
            // Em Record, o getter é direto pelo nome do campo (sem "get")
            throw new IllegalArgumentException(
                    "Já existe um usuário com o email: " + request.email()
            );
        }

        // Converte Request → Model usando Builder (padrão que você já conhece)
        Usuario usuario = Usuario.builder()
                .nome(request.nome())
                .email(request.email())
                // Em produção: BCryptPasswordEncoder.encode(request.senha())
                // Por enquanto, guardamos como está (simplificação da Fase 1)
                .senha(request.senha())
                .build();
        // Nota: criadoEm é preenchido pelo @PrePersist — não precisamos setar aqui

        // save(): persiste no banco e retorna a entidade com o ID gerado
        Usuario salvo = usuarioRepository.save(usuario);

        // Converte Model → Response e retorna
        return toResponse(salvo);
    }

    // @Transactional(readOnly = true): operação de leitura — mais eficiente
    // O Hibernate não rastreia mudanças nos objetos (sem dirty checking)
    @Transactional(readOnly = true)
    public Page<UsuarioResponse> listar(Pageable pageable) {
        // findAll(pageable): busca paginada — você já conhece!
        return usuarioRepository.findAll(pageable)
                .map(this::toResponse); // Converte cada entidade para Response
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorId(Long id) {
        // orElseThrow: se não encontrar, lança a exceção
        // EntityNotFoundException: capturada pelo GlobalExceptionHandler → 404
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Usuário não encontrado com ID: " + id
                ));
        return toResponse(usuario);
    }

    @Transactional
    public UsuarioResponse atualizar(Long id, UsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Usuário não encontrado com ID: " + id
                ));

        // Verifica se o email já pertence a OUTRO usuário
        if (!usuario.getEmail().equals(request.email()) &&
                usuarioRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException(
                    "Email já está em uso: " + request.email()
            );
        }

        // Atualiza os campos
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setSenha(request.senha());

        // Com @Transactional, o Hibernate detecta a mudança e faz UPDATE automaticamente
        // Você não precisa chamar save() explicitamente (mas pode — não tem problema)
        Usuario atualizado = usuarioRepository.save(usuario);
        return toResponse(atualizado);
    }

    @Transactional
    public void deletar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuário não encontrado com ID: " + id);
        }
        // deleteById: deleta pelo ID
        // O CASCADE no banco cuida de deletar a carteira e ativos relacionados
        usuarioRepository.deleteById(id);
    }

    // Método privado de conversão Model → Response
    // 'private': não faz sentido expor fora do Service
    // Centraliza a conversão — se o Response mudar, muda em um lugar só
    private UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCriadoEm()
        );
    }
}