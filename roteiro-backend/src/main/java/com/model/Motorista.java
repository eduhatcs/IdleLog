package com.pucminas.roteiro.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Motorista/motoboy responsável por rodar um roteiro (RF01).
 */
@Entity
@Table(name = "motorista")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Motorista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 120)
    private String nome;

    @NotBlank
    @Column(nullable = false, length = 20)
    private String telefone;

    @NotBlank
    @Column(nullable = false, unique = true, length = 30)
    private String documento;

    @NotBlank
    @Column(nullable = false, length = 60)
    private String veiculo;

    /** Usado futuramente no cálculo de custo do trajeto (RN07). */
    @NotNull
    @Positive
    @Column(name = "rendimento_km_litro", nullable = false)
    private Double rendimentoKmLitro;
}
