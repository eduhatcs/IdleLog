package com.dto;

public record MotoristaResponse(
        Long id,
        String nome,
        String telefone,
        String documento,
        String veiculo,
        Double rendimentoKmLitro
) {}
