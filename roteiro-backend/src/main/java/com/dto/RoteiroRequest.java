package com.pucminas.roteiro.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RoteiroRequest(
        @NotNull Long motoristaId,
        @NotNull LocalDate data,
        Double distanciaTotalKm
) {}
