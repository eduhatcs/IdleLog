package com.dto;

import java.math.BigDecimal;

public class ParametrizacaoResponse {

    private Long id;
    private BigDecimal precoCombustivel;
    private BigDecimal kmPorLitroPadrao;
    private BigDecimal jornadaMaximaHoras;
    private BigDecimal custoHoraParada;

    public ParametrizacaoResponse() {}

    public ParametrizacaoResponse(Long id, BigDecimal precoCombustivel, BigDecimal kmPorLitroPadrao, BigDecimal jornadaMaximaHoras, BigDecimal custoHoraParada) {
        this.id = id;
        this.precoCombustivel = precoCombustivel;
        this.kmPorLitroPadrao = kmPorLitroPadrao;
        this.jornadaMaximaHoras = jornadaMaximaHoras;
        this.custoHoraParada = custoHoraParada;
    }

    public Long getId() { return id; }
    public BigDecimal getPrecoCombustivel() { return precoCombustivel; }
    public BigDecimal getKmPorLitroPadrao() { return kmPorLitroPadrao; }
    public BigDecimal getJornadaMaximaHoras() { return jornadaMaximaHoras; }
    public BigDecimal getCustoHoraParada() { return custoHoraParada; }
}