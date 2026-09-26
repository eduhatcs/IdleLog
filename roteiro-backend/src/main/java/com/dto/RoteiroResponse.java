package com.pucminas.roteiro.dto;

import java.time.LocalDate;
import java.util.List;

public record RoteiroResponse(
        Long id,
        LocalDate data,
        Long motoristaId,
        String motoristaNome,
        Double distanciaTotalKm,
        Long tempoTotalParadoMinutos,
        List<PontoResponse> pontos
) {}
