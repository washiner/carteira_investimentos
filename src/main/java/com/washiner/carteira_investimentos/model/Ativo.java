package com.washiner.carteira_investimentos.model;

// Localização: src/main/java/com/washiner/carteira/model/Ativo.java

import com.washiner.carteira_investimentos.enums.TipoAtivo;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ativos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Ativo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ticker é o código do ativo: PETR4, MXRF11, BTC, etc.
    // unique = true: não pode ter dois ativos com o mesmo ticker NA MESMA CARTEIRA
    // (na vida real isso seria por carteira, mas vamos simplificar)
    @Column(nullable = false, length = 20)
    private String ticker;

    @Column(nullable = false, length = 100)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoAtivo tipo;

    // Quantidade de cotas/ações que o usuário possui
    // precision e scale altos porque pode ser 0.00000001 (cripto)
    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal quantidade;

    // Preço médio de compra
    @Column(name = "preco_medio", nullable = false, precision = 19, scale = 4)
    private BigDecimal precoMedio;

    // Valor atual (atualizado manualmente ou por integração futura com API)
    @Column(name = "valor_atual", precision = 19, scale = 4)
    private BigDecimal valorAtual;

    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
    }

    // @PreUpdate: executado ANTES de cada UPDATE no banco
    // Atualiza o timestamp toda vez que o registro é modificado
    @PreUpdate
    public void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }

    // RELACIONAMENTO: Ativo (N) ←→ (1) Carteira
    //
    // A FK fica AQUI porque o Ativo PERTENCE à Carteira
    // Regra: "A FK fica no lado que pertence ao outro"
    //
    // @JoinColumn: cria a coluna carteira_id na tabela ativos
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carteira_id", nullable = false)
    private Carteira carteira;

    // RELACIONAMENTO: Ativo (N) ←→ (N) Categoria
    //
    // @ManyToMany: o Ativo é o lado "dono" da relação (quem tem @JoinTable)
    //
    // @JoinTable: configura a tabela intermediária no banco
    //   name: nome da tabela intermediária
    //   joinColumns: FK que aponta para ESTE lado (Ativo)
    //   inverseJoinColumns: FK que aponta para o OUTRO lado (Categoria)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "ativos_categorias",               // nome da tabela intermediária
            joinColumns = @JoinColumn(name = "ativo_id"),          // FK para Ativo
            inverseJoinColumns = @JoinColumn(name = "categoria_id") // FK para Categoria
    )
    @Builder.Default
    private List<Categoria> categorias = new ArrayList<>();
}