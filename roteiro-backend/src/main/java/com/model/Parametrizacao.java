package com.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "parametrizacao")
public class Parametrizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "preco_combustivel", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoCombustivel;

    @Column(name = "km_por_litro_padrao", nullable = false, precision = 10, scale = 2)
    private BigDecimal kmPorLitroPadrao;

    @Column(name = "jornada_maxima_horas", nullable = false, precision = 10, scale = 2)
    private BigDecimal jornadaMaximaHoras;

    @Column(name = "custo_hora_parada", precision = 10, scale = 2)
    private BigDecimal custoHoraParada;

    public Parametrizacao() {}

    public Parametrizacao(Long id, BigDecimal precoCombustivel, BigDecimal kmPorLitroPadrao, BigDecimal jornadaMaximaHoras, BigDecimal custoHoraParada) {
        this.id = id;
        this.precoCombustivel = precoCombustivel;
        this.kmPorLitroPadrao = kmPorLitroPadrao;
        this.jornadaMaximaHoras = jornadaMaximaHoras;
        this.custoHoraParada = custoHoraParada;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public BigDecimal getPrecoCombustivel() { return precoCombustivel; }
    public void setPrecoCombustivel(BigDecimal precoCombustivel) { this.precoCombustivel = precoCombustivel; }
    public BigDecimal getKmPorLitroPadrao() { return kmPorLitroPadrao; }
    public void setKmPorLitroPadrao(BigDecimal kmPorLitroPadrao) { this.kmPorLitroPadrao = kmPorLitroPadrao; }
    public BigDecimal getJornadaMaximaHoras() { return jornadaMaximaHoras; }
    public void setJornadaMaximaHoras(BigDecimal jornadaMaximaHoras) { this.jornadaMaximaHoras = jornadaMaximaHoras; }
    public BigDecimal getCustoHoraParada() { return custoHoraParada; }
    public void setCustoHoraParada(BigDecimal custoHoraParada) { this.custoHoraParada = custoHoraParada; }
}