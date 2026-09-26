package com.pucminas.roteiro.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record PontoSaidaRequest(
        @NotNull LocalDateTime dataHoraSaida
) {}
