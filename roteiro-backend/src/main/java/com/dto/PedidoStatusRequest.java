package com.dto;

import com.model.StatusPedido;
import jakarta.validation.constraints.NotNull;

public record PedidoStatusRequest(
        @NotNull StatusPedido status
) {}