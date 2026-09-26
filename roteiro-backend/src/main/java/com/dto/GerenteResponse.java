package com.pucminas.roteiro.dto;

public record GerenteResponse(
        Long id,
        String nome,
        String telefone,
        String email
) {}
