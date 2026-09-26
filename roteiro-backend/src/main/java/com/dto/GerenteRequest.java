package com.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record GerenteRequest(
        @NotBlank String nome,
        @NotBlank String telefone,
        @NotBlank @Email String email
) {}
