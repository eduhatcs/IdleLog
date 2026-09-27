package com.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ParametrizacaoRequest {

    @NotNull(message = "O preço do combustível é obrigatório")
    @DecimalMin(value = "0.01", message = "O preço do combustível deve ser maior que zero")
    private BigDecimal precoCombustivel;

    @NotNull(message = "O km/litro padrão é obrigatório")
    @DecimalMin(value = "0.1", message = "O km/litro padrão deve ser maior que zero")
    private BigDecimal kmPorLitroPadrao;

    @NotNull(message = "A jornada máxima em horas é obrigatória")
    @DecimalMin(value = "0.1", message = "A jornada máxima deve ser maior que zero")
    private BigDecimal jornadaMaximaHoras;

    private BigDecimal custoHoraParada;

    public ParametrizacaoRequest() {}

    public BigDecimal getPrecoCombustivel() { return precoCombustivel; }
    public void setPrecoCombustivel(BigDecimal precoCombustivel) { this.precoCombustivel = precoCombustivel; }
    public BigDecimal getKmPorLitroPadrao() { return kmPorLitroPadrao; }
    public void setKmPorLitroPadrao(BigDecimal kmPorLitroPadrao) { this.kmPorLitroPadrao = kmPorLitroPadrao; }
    public BigDecimal getJornadaMaximaHoras() { return jornadaMaximaHoras; }
    public void setJornadaMaximaHoras(BigDecimal jornadaMaximaHoras) { this.jornadaMaximaHoras = jornadaMaximaHoras; }
    public BigDecimal getCustoHoraParada() { return custoHoraParada; }
    public void setCustoHoraParada(BigDecimal custoHoraParada) { this.custoHoraParada = custoHoraParada; }
}