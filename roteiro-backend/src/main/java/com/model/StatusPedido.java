package com.model;

/**
 * Situação de um pedido do cadastro até a entrega.
 * O diagrama de classes conceitual só tem o atributo "status" (String);
 * aqui ele foi modelado como enum para evitar valores livres/inconsistentes.
 */
public enum StatusPedido {
    PENDENTE,
    EM_ROTA,
    ENTREGUE,
    CANCELADO
}