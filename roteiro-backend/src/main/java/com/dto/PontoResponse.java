package com.pucminas.roteiro.dto;

import java.time.LocalDateTime;

public record PontoResponse(
        Long id,
        String endereco,
        Double latitude,
        Double longitude,
        Integer ordem,
        LocalDateTime dataHoraChegada,
        LocalDateTime dataHoraSaida,
        Long tempoParadoMinutos
) {}
