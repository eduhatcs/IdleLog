package com.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record PontoRequest(
        @NotBlank String endereco,
        Double latitude,
        Double longitude,
        @NotNull @Positive Integer ordem,
        @NotNull LocalDateTime dataHoraChegada,
        LocalDateTime dataHoraSaida,
        Long pedidoId
) {}
