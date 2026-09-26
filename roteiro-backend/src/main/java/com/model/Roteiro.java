package com.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Roteiro diário de um motorista, composto por pontos em ordem sequencial (RF04).
 * RN05: cada roteiro pertence a um único motorista e a uma única data
 * (por isso a constraint de unicidade motorista_id + data).
 */
@Entity
@Table(name = "roteiro", uniqueConstraints = @UniqueConstraint(columnNames = {"motorista_id", "data"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Roteiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private LocalDate data;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "motorista_id", nullable = false)
    private Motorista motorista;

    /** Distância percorrida no roteiro; usada futuramente no cálculo de custo (RN07, fora deste escopo). */
    @Column(name = "distancia_total_km")
    private Double distanciaTotalKm;

    /** RN03: soma dos tempos parados de todos os pontos, exceto o ponto de partida. Recalculado a cada ponto. */
    @Column(name = "tempo_total_parado_minutos")
    private Long tempoTotalParadoMinutos = 0L;

    @OneToMany(mappedBy = "roteiro", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("ordem ASC")
    @JsonIgnore
    private List<Ponto> pontos = new ArrayList<>();
}
