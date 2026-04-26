package com.washiner.carteira_investimentos.model;

// Localização: src/main/java/com/washiner/carteira/model/Usuario.java

// Imports do JPA — mapeamento objeto-relacional
import com.washiner.carteira_investimentos.CarteiraInvestimentosApplication;
import jakarta.persistence.*;
import lombok.*;
// Imports do Lombok — gera código automático em tempo de compilação

import java.time.LocalDateTime;

// @Entity: diz ao JPA que essa classe representa uma tabela no banco
// Sem @Entity, o JPA ignora a classe completamente
@Entity

// @Table: configura detalhes da tabela
// name = nome real da tabela no banco (boa prática: plural, snake_case)
@Table(name = "usuarios")

// @Getter: Lombok gera getters para todos os campos
// @Setter: Lombok gera setters para todos os campos
// @NoArgsConstructor: Lombok gera construtor sem argumentos (JPA exige isso)
// @AllArgsConstructor: Lombok gera construtor com todos os argumentos
// @Builder: Lombok gera o padrão Builder (Usuario.builder().nome("João").build())
// @EqualsAndHashCode: Lombok gera equals() e hashCode() baseado no 'id'
//   callSuper = false: não inclui campos da classe pai (Object)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Usuario {

    // @Id: marca este campo como chave primária
    @Id

    // @GeneratedValue: o banco gera o valor automaticamente
    // IDENTITY = usa o recurso SERIAL/IDENTITY do banco (mais eficiente no PostgreSQL)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column: configura detalhes da coluna
    // nullable = false: não aceita null (equivale a NOT NULL no SQL)
    // unique = true: cada usuário tem um email único
    // length = 100: tamanho máximo do varchar
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String nome;

    // Senha nunca deve ser salva em texto puro
    // Em Fase 3 (Spring Security), você vai aprender a fazer hash
    @Column(nullable = false)
    private String senha;

    // @Column(name = "criado_em"): nome explícito da coluna no banco
    // updatable = false: uma vez criado, não pode ser alterado via JPA
    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;

    // @PrePersist: método executado ANTES de salvar no banco pela primeira vez
    // Perfeito para preencher campos automáticos como timestamps
    @PrePersist
    public void prePersist() {
        // LocalDateTime.now() pega a data/hora atual do servidor
        this.criadoEm = LocalDateTime.now();
    }

    // RELACIONAMENTO: Usuario (1) ←→ (1) Carteira
    //
    // Por que a FK NÃO fica no Usuario?
    // Lógica de negócio: a Carteira PERTENCE ao Usuário
    // A Carteira é que "sabe" a quem pertence, então ela guarda a FK
    //
    // @OneToOne: um Usuário tem uma Carteira
    // mappedBy = "usuario": diz ao JPA que o OUTRO lado (@OneToOne na Carteira)
    //            tem o campo "usuario" que contém a FK real
    //            mappedBy = "estou mapeado por aquele campo lá"
    // cascade = ALL: qualquer operação no Usuário se propaga para a Carteira
    //   Se salvar o Usuário → salva a Carteira junto
    //   Se deletar o Usuário → deleta a Carteira junto
    // fetch = LAZY: só carrega a Carteira do banco quando você realmente acessar
    //   Sem LAZY, toda consulta de Usuário já puxaria a Carteira também (ineficiente)
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Carteira carteira;
}
