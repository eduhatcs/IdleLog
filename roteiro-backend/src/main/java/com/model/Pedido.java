package com.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Pedido feito por um cliente, com o endereço de entrega que futuramente
 * pode virar um Ponto de um roteiro (RF - "Gerenciar Pedidos" e
 * "Associar Pedidos ao Roteiro" no caso de uso).
 * Relação "realiza" do diagrama de classes: Cliente 1 -- 0..* Pedido.
 */
@Entity
@Table(name = "pedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private LocalDate data;

    @NotBlank
    @Column(name = "endereco_entrega", nullable = false, length = 200)
    private String enderecoEntrega;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusPedido status = StatusPedido.PENDENTE;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
}