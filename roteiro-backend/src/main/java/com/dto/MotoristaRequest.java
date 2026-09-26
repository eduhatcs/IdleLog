package com.pucminas.roteiro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MotoristaRequest(
        @NotBlank String nome,
        @NotBlank String telefone,
        @NotBlank String documento,
        @NotBlank String veiculo,
        @NotNull @Positive Double rendimentoKmLitro
) {}
