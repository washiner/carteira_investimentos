package com.washiner.carteira_investimentos.model;

// Localização: src/main/java/com/washiner/carteira/model/Categoria.java

import com.washiner.carteira_investimentos.enums.TipoCategoria;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categorias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    // @Enumerated(EnumType.STRING): salva o NOME do enum como texto no banco
    // Alternativa: EnumType.ORDINAL salva o índice (0, 1, 2...) — EVITE
    // Por quê evitar ORDINAL? Se você mudar a ordem dos valores no Enum,
    // os dados do banco ficam inconsistentes. Com STRING, isso não acontece.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TipoCategoria tipo;

    @Column(length = 255)
    private String descricao;

    // RELACIONAMENTO: Categoria (N) ←→ (N) Ativo
    //
    // A @JoinTable NÃO fica aqui — fica no Ativo
    // Por convenção, o @JoinTable fica no lado "dono" da relação
    // Aqui usamos mappedBy para dizer que o Ativo é o dono
    //
    // mappedBy = "categorias": o campo 'categorias' dentro de Ativo
    //            é onde está configurada a tabela intermediária
    @ManyToMany(mappedBy = "categorias", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Ativo> ativos = new ArrayList<>();
}
