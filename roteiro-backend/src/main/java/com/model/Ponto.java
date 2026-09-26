package com.pucminas.roteiro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Ponto de parada dentro de um roteiro (endereço + horários de chegada/saída).
 * RN01: o ponto de partida (ordem = 1) não conta tempo parado.
 * RN02: tempo parado = data/hora de saída - data/hora de chegada.
 * RN06: os pontos de um roteiro possuem ordem sequencial (1, 2, 3, 4...).
 */
@Entity
@Table(name = "ponto", uniqueConstraints = @UniqueConstraint(columnNames = {"roteiro_id", "ordem"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ponto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 200)
    private String endereco;

    private Double latitude;

    private Double longitude;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Integer ordem;

    @NotNull
    @Column(name = "data_hora_chegada", nullable = false)
    private LocalDateTime dataHoraChegada;

    @Column(name = "data_hora_saida")
    private LocalDateTime dataHoraSaida;

    /** Calculado pela camada de serviço; nulo para o ponto de partida (RN01) ou enquanto não há saída registrada. */
    @Column(name = "tempo_parado_minutos")
    private Long tempoParadoMinutos;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "roteiro_id", nullable = false)
    @JsonIgnore
    private Roteiro roteiro;
}
