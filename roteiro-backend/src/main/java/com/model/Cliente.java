package com.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Cliente que realiza pedidos (RF - "Gerenciar Clientes" no caso de uso).
 * Relação "realiza" do diagrama de classes: Cliente 1 -- 0..* Pedido.
 */
@Entity
@Table(name = "cliente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 120)
    private String nome;

    @NotBlank
    @Column(nullable = false, length = 20)
    private String telefone;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true, length = 120)
    private String email;
}