package com.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PedidoRequest(
        @NotNull Long clienteId,
        @NotNull LocalDate data,
        @NotBlank String enderecoEntrega
) {}