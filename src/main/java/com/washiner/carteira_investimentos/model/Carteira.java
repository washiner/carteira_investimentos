package com.washiner.carteira_investimentos.model;

// Localização: src/main/java/com/washiner/carteira/model/Carteira.java

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carteiras")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Carteira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    // BigDecimal: SEMPRE use para valores monetários
    // Double e Float têm erros de precisão (0.1 + 0.2 = 0.30000000000000004)
    // BigDecimal não tem esse problema
    // precision = 19: total de dígitos (15 antes da vírgula + 4 depois)
    // scale = 4: casas decimais (ex: 1234.5678)
    @Column(precision = 19, scale = 4)
    private BigDecimal valorTotal;

    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
        // Inicializa com zero ao criar uma carteira nova
        if (this.valorTotal == null) {
            this.valorTotal = BigDecimal.ZERO;
        }
    }

    // RELACIONAMENTO: Carteira (N) ←→ (1) Usuario
    //
    // A FK fica AQUI na Carteira, porque a Carteira PERTENCE ao Usuário
    // Regra: "A FK fica no lado que pertence ao outro"
    // Carteira pertence ao Usuário → FK de usuario_id fica na tabela carteiras
    //
    // @OneToOne: do lado da Carteira, ela tem UM Usuário
    // @JoinColumn: define como a FK se chama no banco
    //   name = "usuario_id": nome da coluna FK na tabela carteiras
    //   nullable = false: toda carteira DEVE ter um usuário
    //   unique = true: garante que um usuário não tenha duas carteiras
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    // RELACIONAMENTO: Carteira (1) ←→ (N) Ativo
    //
    // Uma Carteira tem MUITOS Ativos
    // A FK NÃO fica aqui — fica no Ativo (@ManyToOne lá)
    // mappedBy = "carteira": o campo 'carteira' dentro de Ativo tem a FK
    //
    // cascade = ALL: se salvar/deletar Carteira, propaga para os Ativos
    // orphanRemoval = true: se remover um Ativo da lista, deleta do banco também
    //   Sem isso, o Ativo ficaria "órfão" no banco sem carteira
    @OneToMany(mappedBy = "carteira", cascade = CascadeType.ALL, orphanRemoval = true)

    // Inicializa a lista vazia — evita NullPointerException
    // Boa prática: sempre inicialize coleções
    @Builder.Default
    private List<Ativo> ativos = new ArrayList<>();
}