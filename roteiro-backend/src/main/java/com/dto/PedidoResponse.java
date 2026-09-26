package com.dto;

import com.model.StatusPedido;

import java.time.LocalDate;

public record PedidoResponse(
        Long id,
        Long clienteId,
        String clienteNome,
        LocalDate data,
        String enderecoEntrega,
        StatusPedido status
) {}