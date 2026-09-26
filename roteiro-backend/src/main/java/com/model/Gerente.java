package com.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Gerente/coordenador responsável por acompanhar os roteiros (RF02).
 */
@Entity
@Table(name = "gerente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Gerente {

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
