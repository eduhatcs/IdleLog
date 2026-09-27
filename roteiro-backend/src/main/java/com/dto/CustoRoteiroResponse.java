package com.dto;

import java.math.BigDecimal;

public class CustoRoteiroResponse {

    private Long roteiroId;
    private String data;
    private Long motoristaId;
    private String motoristaNome;
    private BigDecimal distanciaTotalKm;
    private BigDecimal rendimentoKmLitro;
    private BigDecimal precoCombustivel;
    private BigDecimal consumoLitros;
    private BigDecimal custoCombustivel;
    
    // Altere de Integer para Long:
    private Long tempoTotalParadoMinutos;
    
    private BigDecimal tempoTotalParadoHoras;
    private BigDecimal custoHoraParada;
    private BigDecimal custoTempoParado;
    private BigDecimal custoTotalEstimado;
    private BigDecimal jornadaMaximaHoras;
    private Boolean jornadaExcedida;

    public CustoRoteiroResponse() {}

    public Long getRoteiroId() { return roteiroId; }
    public void setRoteiroId(Long roteiroId) { this.roteiroId = roteiroId; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public Long getMotoristaId() { return motoristaId; }
    public void setMotoristaId(Long motoristaId) { this.motoristaId = motoristaId; }

    public String getMotoristaNome() { return motoristaNome; }
    public void setMotoristaNome(String motoristaNome) { this.motoristaNome = motoristaNome; }

    public BigDecimal getDistanciaTotalKm() { return distanciaTotalKm; }
    public void setDistanciaTotalKm(BigDecimal distanciaTotalKm) { this.distanciaTotalKm = distanciaTotalKm; }

    public BigDecimal getRendimentoKmLitro() { return rendimentoKmLitro; }
    public void setRendimentoKmLitro(BigDecimal rendimentoKmLitro) { this.rendimentoKmLitro = rendimentoKmLitro; }

    public BigDecimal getPrecoCombustivel() { return precoCombustivel; }
    public void setPrecoCombustivel(BigDecimal precoCombustivel) { this.precoCombustivel = precoCombustivel; }

    public BigDecimal getConsumoLitros() { return consumoLitros; }
    public void setConsumoLitros(BigDecimal consumoLitros) { this.consumoLitros = consumoLitros; }

    public BigDecimal getCustoCombustivel() { return custoCombustivel; }
    public void setCustoCombustivel(BigDecimal custoCombustivel) { this.custoCombustivel = custoCombustivel; }

    public Long getTempoTotalParadoMinutos() { return tempoTotalParadoMinutos; }
    public void setTempoTotalParadoMinutos(Long tempoTotalParadoMinutos) { this.tempoTotalParadoMinutos = tempoTotalParadoMinutos; }

    public BigDecimal getTempoTotalParadoHoras() { return tempoTotalParadoHoras; }
    public void setTempoTotalParadoHoras(BigDecimal tempoTotalParadoHoras) { this.tempoTotalParadoHoras = tempoTotalParadoHoras; }

    public BigDecimal getCustoHoraParada() { return custoHoraParada; }
    public void setCustoHoraParada(BigDecimal custoHoraParada) { this.custoHoraParada = custoHoraParada; }

    public BigDecimal getCustoTempoParado() { return custoTempoParado; }
    public void setCustoTempoParado(BigDecimal custoTempoParado) { this.custoTempoParado = custoTempoParado; }

    public BigDecimal getCustoTotalEstimado() { return custoTotalEstimado; }
    public void setCustoTotalEstimado(BigDecimal custoTotalEstimado) { this.custoTotalEstimado = custoTotalEstimado; }

    public BigDecimal getJornadaMaximaHoras() { return jornadaMaximaHoras; }
    public void setJornadaMaximaHoras(BigDecimal jornadaMaximaHoras) { this.jornadaMaximaHoras = jornadaMaximaHoras; }

    public Boolean getJornadaExcedida() { return jornadaExcedida; }
    public void setJornadaExcedida(Boolean jornadaExcedida) { this.jornadaExcedida = jornadaExcedida; }
}