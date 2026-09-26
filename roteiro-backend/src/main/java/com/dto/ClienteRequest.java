package com.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ClienteRequest(
        @NotBlank String nome,
        @NotBlank String telefone,
        @NotBlank @Email String email
) {}